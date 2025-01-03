package top.verytouch.vkit.mydoc.builder.file;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.util.ResourceUtil;
import freemarker.cache.ByteArrayTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import top.verytouch.vkit.mydoc.builder.DocBuilder;
import top.verytouch.vkit.mydoc.config.ConfigStorage;
import top.verytouch.vkit.mydoc.constant.DocType;
import top.verytouch.vkit.mydoc.model.ApiModel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 使用freemarker渲染模板文件
 *
 * @author verytouch
 * @since 2021-12
 */
public class FreemarkerDocBuilder extends OutputStreamDocBuilder {

    public FreemarkerDocBuilder(AnActionEvent event, DocType docType) {
        super(event, docType);
    }

    @Override
    protected OutputStream buildOutputStream(ApiModel model) throws IOException {
        // 模板文件
        InputStream inputStream = getTemplateResourceAsStream(docType.getTemplateFileName(), model.getConfig());
        ByteArrayOutputStream resourceOut = new ByteArrayOutputStream();
        IOUtils.copy(inputStream, resourceOut);
        ByteArrayTemplateLoader templateLoader = new ByteArrayTemplateLoader();
        templateLoader.putTemplate(docType.getName(), resourceOut.toByteArray());
        // 模板引擎设置
        Configuration configuration = new Configuration(Configuration.getVersion());
        configuration.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configuration.setClassicCompatible(true);
        configuration.setTemplateLoader(templateLoader);
        // 渲染输出
        Template template = configuration.getTemplate(docType.getName());
        OutputStream outputStream = newOutputStream(model);
        OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        try {
            template.process(model, streamWriter);
        } catch (TemplateException e) {
            throw new IOException(e);
        }
        streamWriter.flush();
        return outputStream;
    }

    /**
     * 获取模板文件
     * 如果配置了templateDir，从配置该处获取。否则获取默认的
     *
     * @param filename 文件名称
     */
    protected InputStream getTemplateResourceAsStream(String filename, ConfigStorage config) throws IOException {
        String templateDir = config.getTemplateDir();
        if (StringUtils.isBlank(templateDir)) {
            InputStream stream = ResourceUtil.getResourceAsStream(DocBuilder.class.getClassLoader(),
                    DocType.TEMPLATE_DIR, filename);
            if (stream == null) {
                throw new RuntimeException("template file not found: " + filename);
            }
            return stream;
        }
        // 自定义模板
        File fileDir = new File(templateDir);
        if (!fileDir.exists() || !fileDir.isDirectory()) {
            throw new RuntimeException("template dir not found: " + templateDir);
        }
        File file = new File(templateDir + File.separator + filename);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("template file not found: " + templateDir +
                    File.separator + filename);
        }
        return Files.newInputStream(file.toPath());
    }

    protected OutputStream newOutputStream(ApiModel model) throws IOException {
        return Files.newOutputStream(Paths.get(getOutPath(model)));
    }

}
