package integration.panel;

import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.panels.NonOpaquePanel;
import com.intellij.ui.table.TableView;
import integration.domain.StackOverflowQuestion;

import javax.swing.*;
import java.awt.*;

public class TableResultsPanel extends NonOpaquePanel
{
    private TableView<StackOverflowQuestion> resultsTable;

    public TableResultsPanel(TableView<StackOverflowQuestion> resultsTable)
    {
        this.resultsTable = resultsTable;
        this.init();
    }

    private void init()
    {
        this.setBorder(BorderFactory.createEmptyBorder());
        JPanel scrollPanel = new JPanel();
        scrollPanel.setBorder(BorderFactory.createEmptyBorder());
        scrollPanel.setLayout(new BorderLayout());
        scrollPanel.add(ScrollPaneFactory.createScrollPane(this.resultsTable), BorderLayout.CENTER);
        this.setLayout(new BorderLayout());
        this.add(scrollPanel, BorderLayout.CENTER);
    }
}
