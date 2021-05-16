package integration.panel;

import com.intellij.icons.AllIcons;
import com.intellij.ide.plugins.newui.ListPluginComponent;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.SearchTextField;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.StatusText;
import integration.domain.StackOverflowQuestion;
import integration.service.StackOverflowRestService;
import integration.table.SOTableModel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class SearchPanel extends NonOpaquePanel
{
    private static final Logger LOG = Logger.getInstance(SearchPanel.class);

    private SearchTextField tagSearchField;
    private SearchTextField titleSearchField;
    private final TableView<StackOverflowQuestion> resultsTable;
    private final SOTableModel soTableModel;

    public SearchPanel(SOTableModel tableModel, TableView<StackOverflowQuestion> resultsTable)
    {
        this.soTableModel = tableModel;
        this.resultsTable = resultsTable;
        this.init();
    }

    private void init()
    {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createEmptyBorder());

        this.tagSearchField = new SearchTextField();
        String tagText = "Search by Tags";
        StatusText tagSearchEmptyText = this.tagSearchField.getTextEditor().getEmptyText();
        tagSearchEmptyText.appendText(tagText, new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, ListPluginComponent.GRAY_COLOR));
        this.tagSearchField.addKeyboardListener(this.keyAdapterSearchStackOverflow());

        this.titleSearchField = new SearchTextField();
        String titleText = "Search by Title";
        StatusText titleSearchEmptyText = this.titleSearchField.getTextEditor().getEmptyText();
        titleSearchEmptyText.appendText(titleText, new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, ListPluginComponent.GRAY_COLOR));
        this.titleSearchField.addKeyboardListener(this.keyAdapterSearchStackOverflow());

        this.add(this.tagSearchField);
        this.add(this.titleSearchField);
        ActionToolbar toolbar = this.createToolbar();
        toolbar.setTargetComponent(this);
        this.add(toolbar.getComponent());
    }

    @NotNull
    private ActionToolbar createToolbar()
    {
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new SearchAction());
        actionGroup.add(new ClearTableAction());
        return ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actionGroup, true);
    }

    @NotNull
    private KeyAdapter keyAdapterSearchStackOverflow()
    {
        return new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (KeyEvent.VK_ENTER == e.getKeyCode())
                {
                    SearchPanel.this.searchStackOverflow();
                }
            }
        };
    }

    private void searchStackOverflow()
    {
        List<StackOverflowQuestion> stackOverflowPosts = new ArrayList<>();
        try
        {
            stackOverflowPosts = StackOverflowRestService.getStackOverflowPosts(this.tagSearchField.getText(), this.titleSearchField.getText());
        }
        catch (IOException | URISyntaxException e)
        {
            LOG.error(e.getMessage());
        }
        if (stackOverflowPosts.isEmpty())
        {
            this.resultsTable.getEmptyText().setText("No results found for your search criteria");
        }
        this.soTableModel.setItems(stackOverflowPosts);
        this.resultsTable.updateColumnSizes();
    }

    private void clearResults()
    {
        this.soTableModel.setItems(new ArrayList<>());
        this.tagSearchField.setText("");
        this.titleSearchField.setText("");
    }

    public class SearchAction extends DumbAwareAction
    {
        protected SearchAction()
        {
            super("Get StackOverflow issues", "Get StackOverflow issues", AllIcons.Actions.Execute);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e)
        {
            SearchPanel.this.searchStackOverflow();
        }
    }

    public class ClearTableAction extends DumbAwareAction
    {
        protected ClearTableAction()
        {
            super("Clear Inputs and Results", "Clear inputs and results", AllIcons.Actions.Refresh);
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e)
        {
            SearchPanel.this.clearResults();
        }
    }
}
