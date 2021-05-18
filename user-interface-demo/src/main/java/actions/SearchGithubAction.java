package actions;

import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.util.io.URLUtil;
import org.jetbrains.annotations.NotNull;

public class SearchGithubAction extends DumbAwareAction
{
    @Override
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Editor ediTorRequiredData = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = ediTorRequiredData.getCaretModel();
        String selectedText = caretModel.getCurrentCaret().getSelectedText();

        String gitHubQuery = "https://github.com/search?q=%s&type=code";
        String searchBy = String.format(gitHubQuery, URLUtil.encodeURIComponent(selectedText));
        BrowserUtil.browse(searchBy);
    }

    @Override
    public void update(@NotNull AnActionEvent e)
    {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        CaretModel caretModel = editor.getCaretModel();
        String selectedText = caretModel.getCurrentCaret().getSelectedText();
        if (selectedText.isEmpty())
        {
            e.getPresentation().setVisible(false);
        }
    }
}
