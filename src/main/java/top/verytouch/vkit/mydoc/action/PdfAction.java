package top.verytouch.vkit.mydoc.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import top.verytouch.vkit.mydoc.builder.BuilderTask;
import top.verytouch.vkit.mydoc.builder.file.PdfDocBuilder;

/**
 * 导出为pdf
 *
 * @author verytouch
 * @since 2024-12
 */
public class PdfAction extends AbstractMyAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        BuilderTask.start(new PdfDocBuilder(event));
    }

}
