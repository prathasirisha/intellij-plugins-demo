package integration.table;

import com.intellij.ide.BrowserUtil;
import com.intellij.ui.table.TableView;
import com.intellij.util.ui.ListTableModel;
import integration.domain.StackOverflowQuestion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ResultsTable extends TableView<StackOverflowQuestion>
{
    public ResultsTable(ListTableModel<StackOverflowQuestion> model)
    {
        super(model);
        this.init();
    }

    private void init()
    {
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setCellSelectionEnabled(true);
        this.setStriped(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setAutoCreateRowSorter(true);
        this.getEmptyText().setText("Search for StackOverflow questions!");
        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                JTable source = (JTable) e.getSource();
                int rowAtPoint = source.rowAtPoint(e.getPoint());
                int columnAtPoint = source.columnAtPoint(e.getPoint());
                // Link column = 2
                if (2 == columnAtPoint && MouseEvent.BUTTON1 == e.getButton())
                {
                    BrowserUtil.browse(ResultsTable.this.getValueAt(rowAtPoint, columnAtPoint).toString());
                }
                super.mousePressed(e);
            }
        });
    }
}
