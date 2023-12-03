import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BookTicketPanel extends JPanel {
    private JSpinner numberOfTicketsSpinner;
    private JRadioButton adultRadio, seniorRadio, studentRadio;
    private JToggleButton[][] seatButtons;
    private JTextField totalPriceField;
    private JButton bookTicketButtonInPanel;

    private JComboBox<String> musicalComboBox;
    private JComboBox<String> dateComboBox;
    private JComboBox<String> timeComboBox;

    private List<String> musicals;
    private List<String> dates;
    private List<String> times;

    private String selectedMusical;
    private String selectedDate;
    private String selectedTime;
    private String selectedVenue = "Venue1"; // Assuming a default venue for now

    public BookTicketPanel() {
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);

        // Musical Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Musical:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        musicalComboBox = new JComboBox<>();
        add(musicalComboBox, gbc);

        // Initialize musicals (loadMusicals method will be called initially)
        loadMusicals();
        selectedMusical = musicalComboBox.getSelectedItem().toString(); // Initialize selectedMusical

        // Date Selection
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Date:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        dateComboBox = new JComboBox<>();
        add(dateComboBox, gbc);

        // Time Selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Time:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        timeComboBox = new JComboBox<>();
        add(timeComboBox, gbc);

        // Number of Tickets
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(new JLabel("Number of Tickets:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        numberOfTicketsSpinner = new JSpinner();
        JComponent editor = numberOfTicketsSpinner.getEditor();
        Dimension prefSize = editor.getPreferredSize();
        prefSize = new Dimension(70, 30);
        editor.setPreferredSize(prefSize);
        add(numberOfTicketsSpinner, gbc);

        // Ticket Type
        gbc.gridx = 0;
        gbc.gridy = 4;
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
        gbc.gridy = 4;
        add(ticketTypePanel, gbc);

        // Total Price
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Total Price:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        totalPriceField = new JTextField(10);
        totalPriceField.setEditable(false);
        totalPriceField.setBackground(Color.LIGHT_GRAY);
        add(totalPriceField, gbc);

        // Book Ticket Button
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        bookTicketButtonInPanel = new JButton("Book Ticket");
        bookTicketButtonInPanel.setBackground(new Color(30, 144, 255));
        bookTicketButtonInPanel.setForeground(Color.WHITE);
        add(bookTicketButtonInPanel, gbc);

        bookTicketButtonInPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
                saveBookingToCSV(); // Save the booking details to CSV
            }
        });

        // Load seat availability from CSV
        loadSeatAvailabilityFromCSV();

        // Add listeners
        addListeners();

    }

    private void saveBookingToCSV() {
        try {
            File file = new File("bookings.csv");
            boolean fileExists = file.exists();

            FileWriter writer = new FileWriter(file, true); // Append mode
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            PrintWriter printWriter = new PrintWriter(bufferedWriter);

            // Write header if the file is empty
            if (!fileExists) {
                printWriter.println("Musical,Time,Date,Venue,SeatRow,SeatColumn,TotalPrice,TicketType");
            }

            // Get the selected ticket type
            String ticketType = "";
            if (adultRadio.isSelected()) {
                ticketType = "Adult";
            } else if (seniorRadio.isSelected()) {
                ticketType = "Senior";
            } else if (studentRadio.isSelected()) {
                ticketType = "Student";
            }

            // Write the booking details to the file
            for (int i = 0; i < seatButtons.length; i++) {
                for (int j = 0; j < seatButtons[i].length; j++) {
                    if (seatButtons[i][j].isSelected()) {
                        printWriter.printf("%s,%s,%s,%s,%d,%d,%.2f,%s%n",
                                selectedMusical, selectedTime, selectedDate, selectedVenue,
                                i + 1, j + 1, Double.parseDouble(totalPriceField.getText()), ticketType);
                    }
                }
            }

            printWriter.close();
            bufferedWriter.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSeatSelectionPanel(int rows, int columns) {
        JPanel seatPanel = new JPanel(new GridLayout(rows, columns, 5, 5));
        seatPanel.setName("seatPanel"); // Set a name for easy identification and removal
        seatButtons = new JToggleButton[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seatButtons[i][j] = new JToggleButton(String.format("Seat %d-%d", i + 1, j + 1));
                seatPanel.add(seatButtons[i][j]);
            }
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(seatPanel, gbc);

        revalidate();
        repaint();
    }

    private void loadMusicals() {
        musicals = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("seat_availability.csv"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String musical = parts[0].trim();
                if (!musicals.contains(musical)) {
                    musicals.add(musical);
                }
            }
            musicalComboBox.setModel(new DefaultComboBoxModel<>(musicals.toArray(new String[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeSeatPanel() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && component.getName() != null && component.getName().equals("seatPanel")) {
                remove(component);
                break;
            }
        }
        revalidate();
        repaint();
    }

    private void loadDatesAndSeats() {
        dates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("seat_availability.csv"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String musical = parts[0].trim();
                String date = parts[2].trim();
                if (musical.equals(selectedMusical) && !dates.contains(date)) {
                    dates.add(date);
                }
            }
            dateComboBox.setModel(new DefaultComboBoxModel<>(dates.toArray(new String[0])));

            // Remove existing seat panel and create a new one
            removeSeatPanel();
            createSeatSelectionPanel(3, 3);

            // Load seat availability based on the selected musical, date, and time
            loadSeatAvailabilityFromCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDates() {
        dates = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("seat_availability.csv"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String musical = parts[0].trim();
                String date = parts[2].trim();
                if (musical.equals(selectedMusical) && !dates.contains(date)) {
                    dates.add(date);
                }
            }
            dateComboBox.setModel(new DefaultComboBoxModel<>(dates.toArray(new String[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTimes() {
        times = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("seat_availability.csv"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String musical = parts[0].trim();
                String date = parts[2].trim();
                String time = parts[1].trim();
                if (musical.equals(selectedMusical) && date.equals(selectedDate) && !times.contains(time)) {
                    times.add(time);
                }
            }
            timeComboBox.setModel(new DefaultComboBoxModel<>(times.toArray(new String[0])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSeatAvailabilityFromCSV() {
        // Load seat availability from CSV and update seat panel
        try (BufferedReader reader = new BufferedReader(new FileReader("seat_availability.csv"))) {
            String header = reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String musical = parts[0].trim();
                String time = parts[1].trim();
                String date = parts[2].trim();
                String venue = parts[3].trim();
                int seatRow = Integer.parseInt(parts[4].trim()) - 1;
                int seatColumn = Integer.parseInt(parts[5].trim()) - 1;
                boolean isAvailable = Boolean.parseBoolean(parts[6].trim());

                if (musical.equals(selectedMusical) && time.equals(selectedTime) &&
                        date.equals(selectedDate) && venue.equals(selectedVenue)) {
                    seatButtons[seatRow][seatColumn].setEnabled(isAvailable);
                    if (!isAvailable) {
                        seatButtons[seatRow][seatColumn].setBackground(Color.RED);
                    } else {
                        seatButtons[seatRow][seatColumn].setBackground(null);
                    }
                }
            }
            revalidate();
            repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateTotalPrice() {
        double basePrice = 10.0;
        int numberOfTickets = 0;

        // Validate and adjust the number of tickets
        try {
            int tempNumberOfTickets = Integer.parseInt(numberOfTicketsSpinner.getValue().toString());
            numberOfTickets = Math.max(tempNumberOfTickets, 0);
            numberOfTicketsSpinner.setValue(numberOfTickets);
        } catch (NumberFormatException e) {
            numberOfTickets = 0;
            numberOfTicketsSpinner.setValue(0);
        }

        double totalPrice = 0.0;
        boolean seatsAvailable = true;

        for (int i = 0; i < seatButtons.length; i++) {
            for (int j = 0; j < seatButtons[i].length; j++) {
                if (seatButtons[i][j].isSelected() && !seatButtons[i][j].isEnabled()) {
                    seatsAvailable = false;
                    break;
                }
            }
        }

        if (seatsAvailable) {
            if (adultRadio.isSelected()) {
                totalPrice = numberOfTickets * basePrice;
            } else if (seniorRadio.isSelected()) {
                totalPrice = numberOfTickets * (basePrice - 2.0);
            } else if (studentRadio.isSelected()) {
                totalPrice = numberOfTickets * (basePrice - 3.0);
            }
            totalPriceField.setText(String.format("%.2f", totalPrice));
        } else {
            totalPriceField.setText("Seats not available!");
        }
    }

    private void addListeners() {

        musicalComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMusical = musicalComboBox.getSelectedItem().toString();
                loadDates();
            }
        });

        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedDate = dateComboBox.getSelectedItem().toString();
                loadTimes();
            }
        });

        timeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeComboBox.getSelectedItem() != null) {
                    selectedTime = timeComboBox.getSelectedItem().toString();
                    loadDatesAndSeats(); // Load dates and update seats based on the selected time
                }
            }
        });

        musicalComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedMusical = musicalComboBox.getSelectedItem().toString();

                selectedDate = null;
                selectedTime = null;

                dateComboBox.setModel(new DefaultComboBoxModel<>(new String[] { null }));
                timeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { null }));

                loadDatesAndSeats(); // Load dates and update seats based on the selected musical
            }
        });

        dateComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedTime = null;

                timeComboBox.setModel(new DefaultComboBoxModel<>(new String[] { null }));

                loadTimes();

                // Remove existing seat panel and create a new one
                removeSeatPanel();
                createSeatSelectionPanel(3, 3);

                // Load seat availability based on the selected musical, date, and time
                loadSeatAvailabilityFromCSV();

            }
        });

        timeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeComboBox.getSelectedItem() != null) {
                    selectedTime = timeComboBox.getSelectedItem().toString();

                    removeSeatPanel();
                    createSeatSelectionPanel(3, 3); // Create the seat selection panel first
                    loadSeatAvailabilityFromCSV(); // Then load seat availability
                }

            }
        });

        numberOfTicketsSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                updateTotalPrice();
            }
        });

        adultRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
            }
        });

        seniorRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
            }
        });

        studentRadio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPrice();
            }
        });

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Book Ticket Panel Test");
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            BookTicketPanel bookTicketPanel = new BookTicketPanel();
            frame.add(bookTicketPanel);
            frame.setVisible(true);
        });
    }
}
