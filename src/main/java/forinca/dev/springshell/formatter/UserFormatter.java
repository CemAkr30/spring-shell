package forinca.dev.springshell.formatter;

import forinca.dev.springshell.dao.domain.User;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.shell.table.ArrayTableModel;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.TableBuilder;
import org.springframework.stereotype.Component;

@Component
public class UserFormatter {

  private static Object[] toRow(User item) {
    return new Object[] {
        item.getId(),
        item.getUsername(),
        item.getPassword(),
        item.getEmail()
    };
  }

  public String convertToTable(List<User> items) {
    var data = items.stream().map(UserFormatter::toRow).collect(Collectors.toList());

    data.add(0, new String[] {"id","username","password","email"});
    ArrayTableModel tableModel = new ArrayTableModel(data.toArray(Object[][]::new));
    TableBuilder tableBuilder = new TableBuilder(tableModel);

    tableBuilder.addHeaderAndVerticalsBorders(BorderStyle.fancy_light);
    return tableBuilder.build().render(100);
  }
}
