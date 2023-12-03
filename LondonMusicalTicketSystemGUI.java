import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LondonMusicalTicketSystemGUI extends JFrame {
    private JPanel buttonPanel;
    private JButton musicalListButton;
    private JButton showScheduleButton;
    private JButton bookTicketButton;
    private JButton exitButton;

    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Musical Information Panel
    private JPanel musicalInformationPanel;
    private JList<Musical> musicalList;
    private DefaultListModel<Musical> musicalListModel;

    // Show Schedule and Seats Panel
    private JPanel showSchedulePanel;
    private JComboBox<String> musicalDropdown;
    private JComboBox<String> dateDropdown;
    private JComboBox<String> timeDropdown;
    private JList<String> seatsList;

    // Book Ticket Information Panel
    private JPanel bookTicketPanel;
    private JSpinner numberOfTicketsSpinner;
    private JRadioButton adultRadio, seniorRadio, studentRadio;
    private JList<String> selectedSeatsList;
    private JTextField totalPriceField;
    private JButton bookTicketButtonInPanel, printReceiptButton;

    public LondonMusicalTicketSystemGUI() {
        // Setting up the main frame
        setTitle("London Musical Ticket System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Creating buttons panel
        buttonPanel = new JPanel();
        musicalListButton = new JButton("Musical List");
        showScheduleButton = new JButton("Show Schedule");
        bookTicketButton = new JButton("Book Ticket");
        exitButton = new JButton("Exit");

        buttonPanel.add(musicalListButton);
        buttonPanel.add(showScheduleButton);
        buttonPanel.add(bookTicketButton);
        buttonPanel.add(exitButton);

        // Creating content panel with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        // Musical Information Panel
        musicalInformationPanel = new JPanel(new BorderLayout());
        musicalListModel = new DefaultListModel<>();
        musicalList = new JList<>(musicalListModel);
        musicalList.setCellRenderer(new MusicalCellRenderer()); // Set custom renderer
        populateMusicals();
        musicalInformationPanel.add(new JScrollPane(musicalList), BorderLayout.CENTER);
        contentPanel.add(musicalInformationPanel, "MusicalInformation");

        // Show Schedule and Seats Panel
        showSchedulePanel = new JPanel(new GridLayout(4, 2));
        musicalDropdown = new JComboBox<>(new String[] { "The Lion King", "Les Misérables", "Wicked" });
        dateDropdown = new JComboBox<>(new String[] { "Date 1", "Date 2", "Date 3" });
        timeDropdown = new JComboBox<>(new String[] { "Time 1", "Time 2", "Time 3" });
        seatsList = new JList<>(new String[] { "Seat 1", "Seat 2", "Seat 3" });
        showSchedulePanel.add(new JLabel("Select Musical:"));
        showSchedulePanel.add(musicalDropdown);
        showSchedulePanel.add(new JLabel("Select Date:"));
        showSchedulePanel.add(dateDropdown);
        showSchedulePanel.add(new JLabel("Select Time:"));
        showSchedulePanel.add(timeDropdown);
        showSchedulePanel.add(new JLabel("Available Seats:"));
        showSchedulePanel.add(new JScrollPane(seatsList));
        contentPanel.add(showSchedulePanel, "ShowSchedule");

        // Book Ticket Information Panel
        bookTicketPanel = new BookTicketInformationPanel();
        contentPanel.add(bookTicketPanel, "BookTicket");

        // Adding panels to the main frame
        add(buttonPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Setting up action listeners for buttons
        setupActionListeners();
    }

    private void setupActionListeners() {
        musicalListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "MusicalInformation");
            }
        });

        showScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "ShowSchedule");
            }
        });

        bookTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPanel, "BookTicket");
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void populateMusicals() {
        // Add sample musicals to the list model
        musicalListModel.addElement(new Musical("The Lion King", "2h 30m (incl. interval)",
                "Disney Shows, Family and Kids, Last Minute Tickets", "Lyceum Theatre",
                "6+ Children Under 3 are not permitted to enter the theatre"));

        musicalListModel.addElement(new Musical("Les Misérables", "3h (incl. interval)", "Drama, Musicals",
                "Queen's Theatre", "8+"));

        musicalListModel
                .addElement(new Musical("Wicked", "2h 45m (incl. interval)", "Musicals", "Apollo Victoria Theatre",
                        "5+"));
    }

    private class MusicalCellRenderer extends JPanel implements ListCellRenderer<Musical> {
        private JLabel nameLabel;
        private JLabel runTimeLabel;
        private JLabel categoriesLabel;
        private JLabel venueLabel;
        private JLabel ageLabel;

        public MusicalCellRenderer() {
            setLayout(new GridLayout(5, 1));
            nameLabel = new JLabel();
            runTimeLabel = new JLabel();
            categoriesLabel = new JLabel();
            venueLabel = new JLabel();
            ageLabel = new JLabel();

            add(nameLabel);
            add(runTimeLabel);
            add(categoriesLabel);
            add(venueLabel);
            add(ageLabel);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Musical> list, Musical value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value.name.isEmpty()) {
                // This is a gap element, adjust rendering accordingly
                setPreferredSize(new Dimension(0, 10)); // Add some space for the gap
                setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add padding
                return this;
            }

            nameLabel.setText(value.name);
            runTimeLabel.setText("Run Time: " + value.runTime);
            categoriesLabel.setText("Categories: " + value.categories);
            venueLabel.setText("Venue: " + value.venue);
            ageLabel.setText("Age: " + value.age);

            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }

    private class BookTicketInformationPanel extends JPanel {
        private JSpinner numberOfTicketsSpinner;
        private JRadioButton adultRadio, seniorRadio, studentRadio;
        private JList<String> selectedSeatsList;
        private JTextField totalPriceField;
        private JButton bookTicketButtonInPanel;

        public BookTicketInformationPanel() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Number of Tickets
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            add(new JLabel("Number of Tickets:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            numberOfTicketsSpinner = new JSpinner();
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
            selectedSeatsList = new JList<>(new String[] { "Seat 1", "Seat 2" });
            add(new JScrollPane(selectedSeatsList), gbc);

            // Total Price
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(new JLabel("Total Price:"), gbc);

            gbc.gridx = 1;
            gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.WEST; // Align to the left
            totalPriceField = new JTextField(10); // Set the preferred size
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
    }

    private class Musical {
        private String name;
        private String runTime;
        private String categories;
        private String venue;
        private String age;

        public Musical(String name, String runTime, String categories, String venue, String age) {
            this.name = name;
            this.runTime = runTime;
            this.categories = categories;
            this.venue = venue;
            this.age = age;
        }

        @Override
        public String toString() {
            return String.format("%d. %s\nRun Time: %s\nCategories: %s\nVenue: %s\nAge: %s",
                    musicalListModel.size() + 1, name, runTime, categories, venue, age);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LondonMusicalTicketSystemGUI gui = new LondonMusicalTicketSystemGUI();
            gui.setVisible(true);
        });
    }
}
