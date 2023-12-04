import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SchedulePanel extends JPanel {
    private JComboBox<String> dayOfWeekDropdown;
    private JComboBox<String> showDropdown;
    private JTable scheduleTable;
    private JButton updateSeatsButton;
    private JLabel availableSeatsLabel;

    private Map<String, ShowSchedule> showSchedules;

    public SchedulePanel() {
        setLayout(new BorderLayout());

        dayOfWeekDropdown = new JComboBox<>(
                new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" });

        showDropdown = new JComboBox<>(new String[] { "The Lion King", "Wicked" });
        showSchedules = loadShowSchedules();

        String[] columnNames = { "Day of week", "Matinee", "Evening" };
        Object[][] data = getSelectedShowSchedule();
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        scheduleTable = new JTable(tableModel);
        scheduleTable.setPreferredScrollableViewportSize(new Dimension(300, 100));

        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(100);

        scheduleTable.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scheduleTable.setDefaultRenderer(Object.class, centerRenderer);

        updateSeatsButton = new JButton("Update Available Seats");
        updateSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedDayOfWeek = (String) dayOfWeekDropdown.getSelectedItem();
                int selectedRow = getRowIndex(selectedDayOfWeek);
                String selectedMatinee = (String) tableModel.getValueAt(selectedRow, 1);
                String selectedEvening = (String) tableModel.getValueAt(selectedRow, 2);
                String availableSeats = getAvailableSeats(selectedMatinee, selectedEvening);
                availableSeatsLabel.setText("Available Seats: " + availableSeats);
            }
        });

        availableSeatsLabel = new JLabel("Available Seats: ");

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Day of Week:"));
        topPanel.add(dayOfWeekDropdown);
        topPanel.add(new JLabel("Select Show:"));
        topPanel.add(showDropdown);
        topPanel.add(updateSeatsButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        add(availableSeatsLabel, BorderLayout.SOUTH);

        showDropdown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateScheduleTable();
            }
        });
    }

    private Map<String, ShowSchedule> loadShowSchedules() {
        Map<String, ShowSchedule> schedules = new HashMap<>();
        schedules.put("The Lion King", new ShowSchedule("The Lion King", "CSV/schedule.csv"));
        schedules.put("Wicked", new ShowSchedule("Wicked", "CSV/schedule.csv"));
        return schedules;
    }

    private void updateScheduleTable() {
        Object[][] data = getSelectedShowSchedule();
        DefaultTableModel tableModel = (DefaultTableModel) scheduleTable.getModel();
        tableModel.setDataVector(data, new String[] { "Day of week", "Matinee", "Evening" });
    }

    private Object[][] getSelectedShowSchedule() {
        String selectedShow = (String) showDropdown.getSelectedItem();
        ShowSchedule showSchedule = showSchedules.get(selectedShow);
        if (showSchedule != null) {
            return showSchedule.getSchedule();
        }
        return new Object[0][0];
    }

    private int getRowIndex(String dayOfWeek) {
        for (int i = 0; i < scheduleTable.getRowCount(); i++) {
            if (dayOfWeek.equals(scheduleTable.getValueAt(i, 0))) {
                return i;
            }
        }
        return -1;
    }

    private String getAvailableSeats(String matinee, String evening) {
        return "100";
    }

    private static class ShowSchedule {
        private String showName;
        private String scheduleFileName;

        public ShowSchedule(String showName, String scheduleFileName) {
            this.showName = showName;
            this.scheduleFileName = scheduleFileName;
        }

        public String getShowName() {
            return showName;
        }

        public Object[][] getSchedule() {
            return readScheduleFromCSV(scheduleFileName);
        }

        private Object[][] readScheduleFromCSV(String fileName) {
            try {
                Path path = Paths.get(fileName);
                List<String> lines = Files.readAllLines(path);

                Object[][] data = new Object[lines.size()][lines.get(0).split(",").length];

                for (int i = 0; i < lines.size(); i++) {
                    String[] parts = lines.get(i).split(",");
                    for (int j = 0; j < parts.length; j++) {
                        data[i][j] = parts[j].equals("-") ? "" : parts[j];
                    }
                }

                return data;
            } catch (IOException e) {
                e.printStackTrace();
                return new Object[0][0];
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Show Schedule Information");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);

            SchedulePanel showSchedulePanel = new SchedulePanel();
            frame.add(showSchedulePanel);

            frame.setVisible(true);
        });
    }
}
