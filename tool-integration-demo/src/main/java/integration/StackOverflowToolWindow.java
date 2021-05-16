package integration;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.OnePixelSplitter;
import com.intellij.ui.SideBorder;
import integration.panel.TableResultsPanel;
import integration.panel.SearchPanel;
import integration.table.ResultsTable;
import integration.table.SOTableModel;

import javax.swing.*;
import java.util.ArrayList;

public class StackOverflowToolWindow
{
    private final JPanel contentToolWindow;

    public JComponent getContent()
    {
        return this.contentToolWindow;
    }

    public StackOverflowToolWindow()
    {
        this.contentToolWindow = new SimpleToolWindowPanel(true, true);
        SOTableModel tableModel = new SOTableModel(SOTableModel.generateColumnInfo(), new ArrayList<>());
        ResultsTable resultsTable = new ResultsTable(tableModel);

        TableResultsPanel tableResultsPanel = new TableResultsPanel(resultsTable);
        tableResultsPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.TOP | SideBorder.RIGHT));
        SearchPanel searchPanel = new SearchPanel(tableModel, resultsTable);
        searchPanel.setBorder(IdeBorderFactory.createBorder(SideBorder.TOP | SideBorder.RIGHT | SideBorder.BOTTOM));
        OnePixelSplitter horizontalSplitter = new OnePixelSplitter(true, 0.0f);
        horizontalSplitter.setBorder(BorderFactory.createEmptyBorder());
        horizontalSplitter.setDividerPositionStrategy(Splitter.DividerPositionStrategy.KEEP_FIRST_SIZE);
        horizontalSplitter.setResizeEnabled(false);
        horizontalSplitter.setFirstComponent(searchPanel);
        horizontalSplitter.setSecondComponent(tableResultsPanel);
        this.contentToolWindow.add(horizontalSplitter);

    }
}
