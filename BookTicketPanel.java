import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookTicketPanel extends JPanel {
    private JSpinner numberOfTicketsSpinner;
    private JRadioButton adultRadio, seniorRadio, studentRadio;
    private JList<String> selectedSeatsList;
    private JTextField totalPriceField;
    private JButton bookTicketButtonInPanel;

    public BookTicketPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set vertical and horizontal gaps between grids
        gbc.insets = new Insets(20, 10, 10, 5); // 5 pixels of padding on all sides

        // Number of Tickets
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        add(new JLabel("Number of Tickets:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        numberOfTicketsSpinner = new JSpinner();
        // Increase the width of the spinner editor
        JComponent editor = numberOfTicketsSpinner.getEditor();
        Dimension prefSize = editor.getPreferredSize();
        prefSize = new Dimension(70, 30); // Adjust the width as needed
        editor.setPreferredSize(prefSize);
        add(numberOfTicketsSpinner, gbc);

        // Ticket Type
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Ticket Type:"), gbc);

        JPanel ticketTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        adultRadio = new JRadioButton("Adult");
        seniorRadio = new JRadioButton("Senior");
        studentRadio = new JRadioButton("Student");

        ButtonGroup ticketTypeGroup = new ButtonGroup();
        ticketTypeGroup.add(adultRadio);
        ticketTypeGroup.add(seniorRadio);
        ticketTypeGroup.add(studentRadio);

        ticketTypePanel.add(adultRadio);
        ticketTypePanel.add(seniorRadio);
        ticketTypePanel.add(studentRadio);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(ticketTypePanel, gbc);

        // Selected Seats
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Selected Seats:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        selectedSeatsList = new JList<>(new String[]{"Seat 1", "Seat 2"});
        add(new JScrollPane(selectedSeatsList), gbc);

        // Total Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Total Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        totalPriceField = new JTextField(10);
        totalPriceField.setEditable(false); // Set the field to uneditable
        add(totalPriceField, gbc);

        // Book Ticket Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        bookTicketButtonInPanel = new JButton("Book Ticket");
        add(bookTicketButtonInPanel, gbc);

        // Action listener for the "Book Ticket" button
        bookTicketButtonInPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle book ticket button in panel click
                int numberOfTickets = (int) numberOfTicketsSpinner.getValue();
                double basePrice = 10.0; // Set your base ticket price here

                // Calculate total price based on the selected ticket type
                double totalPrice = 0.0;
                if (adultRadio.isSelected()) {
                    totalPrice = numberOfTickets * basePrice;
                } else if (seniorRadio.isSelected()) {
                    totalPrice = numberOfTickets * (basePrice - 2.0); // Senior discount
                } else if (studentRadio.isSelected()) {
                    totalPrice = numberOfTickets * (basePrice - 3.0); // Student discount
                }

                totalPriceField.setText(String.format("%.2f", totalPrice));
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new BookTicketPanel());
            frame.pack();
            frame.setVisible(true);
        });
    }
}
