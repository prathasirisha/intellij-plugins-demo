package integration.table;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.labels.BoldLabel;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import integration.domain.StackOverflowQuestion;

import javax.swing.table.TableCellRenderer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SOTableModel extends ListTableModel<StackOverflowQuestion>
{
    public SOTableModel(ColumnInfo @NotNull [] columnNames, @NotNull List<StackOverflowQuestion> stackOverflowQuestions)
    {
        super(columnNames, stackOverflowQuestions);
    }

    static final String[] COLUMNS = {"Title", "Tags", "Link", "Answer Count", "Created On", "Last Updated"};
    public static ColumnInfo<StackOverflowQuestion, String>[] generateColumnInfo()
    {
        ColumnInfo<StackOverflowQuestion, String>[] columnInfos = new ColumnInfo[COLUMNS.length];
        AtomicInteger i = new AtomicInteger();
        Arrays.stream(COLUMNS).forEach(eachColumn -> {
                    columnInfos[i.get()] = new ColumnInfo<>(eachColumn)
                    {
                        @Nullable
                        @Override
                        public String valueOf(StackOverflowQuestion o)
                        {
                            switch (eachColumn)
                            {
                                case "Tags":
                                    return o.getTags();
                                case "Link":
                                    return o.getLink();
                                case "Title":
                                    return o.getTitle();
                                case "Answer Count":
                                    return o.getAnswerCount().toString();
                                case "Created On":
                                    return o.getCreationDate().toString();
                                case "Last Updated":
                                    return o.getLastActivity().toString();
                                default:
                                    return "Not Available";
                            }
                        }

                        @Override
                        public TableCellRenderer getCustomizedRenderer(StackOverflowQuestion o, TableCellRenderer renderer)
                        {
                            switch (eachColumn)
                            {
                                case "Tags":
                                    return (table, value, isSelected, hasFocus, row, column) -> new BoldLabel(value.toString());
                                case "Link":
                                    return (table, value, isSelected, hasFocus, row, column) -> new LinkLabel<String>(value.toString(), AllIcons.Ide.External_link_arrow);
                                default:
                                    return super.getCustomizedRenderer(o, renderer);
                            }
                        }

                    };
                    i.getAndIncrement();
                }
        );
        return columnInfos;
    }
}
