import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowSchedulePanel extends JPanel {
    private JComboBox<String> dayOfWeekDropdown;
    private JTable scheduleTable;
    private JButton updateSeatsButton;
    private JLabel availableSeatsLabel;

    public ShowSchedulePanel() {
        setLayout(new BorderLayout());

        // Dropdown for selecting day of week
        dayOfWeekDropdown = new JComboBox<>(new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"});

        // Table for displaying show schedule
        String[] columnNames = {"Day of week", "Matinee", "Evening"};
        Object[][] data = {
                {"Monday", "-", "-"},
                {"Tuesday", "-", "-"},
                {"Wednesday", "-", "7:00 pm"},
                {"Thursday", "2:00 pm", "7:00 pm"},
                {"Friday", "-", "7:00 pm"},
                {"Saturday", "2:30 pm", "7:00 pm"},
                {"Sunday", "1:00 pm", "5:30 pm"}
        };
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the cells non-editable
            }
        };
        scheduleTable = new JTable(tableModel);
        scheduleTable.setPreferredScrollableViewportSize(new Dimension(300, 100)); // Adjust the table size

        // Customize column appearance
        scheduleTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Day of week
        scheduleTable.getColumnModel().getColumn(1).setPreferredWidth(100); // Matinee
        scheduleTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Evening

        // Increase the gap between rows
        scheduleTable.setRowHeight(30);

        // Center-align the content in columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        scheduleTable.setDefaultRenderer(Object.class, centerRenderer);

        // Button to update available seats (Replace this with actual logic)
        updateSeatsButton = new JButton("Update Available Seats");
        updateSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Replace this with logic to fetch and update available seats
                String selectedDayOfWeek = (String) dayOfWeekDropdown.getSelectedItem();
                int selectedRow = getRowIndex(selectedDayOfWeek);
                String selectedMatinee = (String) tableModel.getValueAt(selectedRow, 1);
                String selectedEvening = (String) tableModel.getValueAt(selectedRow, 2);
                String availableSeats = getAvailableSeats(selectedMatinee, selectedEvening);
                availableSeatsLabel.setText("Available Seats: " + availableSeats);
            }
        });

        // Label for displaying available seats
        availableSeatsLabel = new JLabel("Available Seats: ");

        // Add components to the panel
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Select Day of Week:"));
        topPanel.add(dayOfWeekDropdown);
        topPanel.add(updateSeatsButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(scheduleTable), BorderLayout.CENTER);
        add(availableSeatsLabel, BorderLayout.SOUTH);
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
        // Replace this with logic to fetch available seats based on the selected schedule
        // For demonstration purposes, this returns a placeholder string
        return "100";
    }
}
