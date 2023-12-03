import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookTicketPanel extends JPanel {
    private JSpinner numberOfTicketsSpinner;
    private JRadioButton adultRadio, seniorRadio, studentRadio;
    private JToggleButton[][] seatButtons; // 2D array to store seat buttons
    private JTextField totalPriceField;
    private JButton bookTicketButtonInPanel;

    public BookTicketPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding to the whole panel
        GridBagConstraints gbc = new GridBagConstraints();

        // Set vertical and horizontal gaps between grids
        gbc.insets = new Insets(10, 5, 10, 5); // 5 pixels of padding on all sides

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
        ticketTypePanel.setBorder(BorderFactory.createTitledBorder("Select Type"));
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
        JPanel seatSelectionPanel = createSeatSelectionPanel(5, 5); // Specify the number of rows and columns
        add(seatSelectionPanel, gbc);

        // Total Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Total Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        totalPriceField = new JTextField(10);
        totalPriceField.setEditable(false); // Set the field to uneditable
        totalPriceField.setBackground(Color.LIGHT_GRAY); // Add a background color
        add(totalPriceField, gbc);

        // Book Ticket Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST; // Align to the left
        bookTicketButtonInPanel = new JButton("Book Ticket");
        bookTicketButtonInPanel.setBackground(new Color(30, 144, 255)); // Use a custom color
        bookTicketButtonInPanel.setForeground(Color.WHITE); // Set text color to white
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

    // Create a panel with a grid of toggle buttons for seat selection
    private JPanel createSeatSelectionPanel(int rows, int columns) {
        JPanel seatPanel = new JPanel(new GridLayout(rows, columns, 5, 5)); // Specify the gaps between grids
        seatButtons = new JToggleButton[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatButtons[i][j] = new JToggleButton(String.format("Seat %d-%d", i + 1, j + 1));
                seatPanel.add(seatButtons[i][j]);
            }
        }

        return seatPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Book Ticket Panel Test");
            frame.setSize(400, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new BookTicketPanel());
            frame.setVisible(true);
        });
    }
}
