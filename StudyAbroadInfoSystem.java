import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;

public class StudyAbroadInfoSystem {
    //All the details are stored in the database, first we have to upload the details.
    static final String DB_URL = "jdbc:mysql://localhost:3306/studyabroad";
    static final String USER = "root";
    static final String PASS = "password";
    
    //GUI components
    static JFrame frame;
    static JTable table;

    public static void main(String[] args) {
        // create and setup GUI
        frame = new JFrame("Study Abroad Information System");
        frame.setSize(1500, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        // Panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon backgroundImageIcon = new ImageIcon("path_to_img");
                Image backgroundImage = backgroundImageIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        Color buttonColor = Color.decode("#F39016");//colour to match my background image
        // Heading panel with GridBagLayout
        JPanel headingPanel = new JPanel(new GridBagLayout());
        headingPanel.setOpaque(false);
        GridBagConstraints headingPanelConstraints = new GridBagConstraints();
        headingPanelConstraints.gridx = 0;
        headingPanelConstraints.gridy = 0;
        headingPanelConstraints.anchor = GridBagConstraints.NORTHWEST; // Align to top left corner
        panel.add(headingPanel, BorderLayout.NORTH); // Add to the NORTH position
    
        // Heading label
        JLabel headingLabel = new JLabel("STUDY ABROAD INFORMATION SYSTEM");
        headingLabel.setFont(new Font("Times New Roman",Font.BOLD, 50));
        headingLabel.setForeground(Color.WHITE);
        GridBagConstraints headingLabelConstraints = new GridBagConstraints();
        headingLabelConstraints.insets = new Insets(20, 0, 0, 0);
        headingPanel.add(headingLabel,headingLabelConstraints);
    
        // Button and search bar panel
        JPanel buttonSearchPanel = new JPanel(new GridBagLayout());
        buttonSearchPanel.setOpaque(false);
        GridBagConstraints buttonSearchPanelConstraints = new GridBagConstraints();
        buttonSearchPanelConstraints.gridx = 0;
        buttonSearchPanelConstraints.gridy = 0;
        buttonSearchPanelConstraints.anchor = GridBagConstraints.WEST; // Align to the left
        panel.add(buttonSearchPanel, BorderLayout.WEST); // Add to the WEST position
    
        // Countries button
        JButton countriesButton = new JButton("Countries");
        styleButton(countriesButton, buttonColor, Color.WHITE);
        GridBagConstraints countriesButtonConstraints = new GridBagConstraints();
        countriesButtonConstraints.gridx = 0;
        countriesButtonConstraints.gridy = 2;
        countriesButtonConstraints.insets = new Insets(10, 10, 10, 10);
        buttonSearchPanel.add(countriesButton, countriesButtonConstraints);

        // Courses button
        JButton coursesButton = new JButton("Courses");
        styleButton(coursesButton,buttonColor, Color.WHITE);
        GridBagConstraints coursesButtonConstraints = new GridBagConstraints();
        coursesButtonConstraints.gridx = 0;
        coursesButtonConstraints.gridy = 3;
        coursesButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(coursesButton, coursesButtonConstraints);

        // About button
        JButton aboutButton = new JButton("About");
        styleButton(aboutButton, buttonColor, Color.WHITE);
        GridBagConstraints aboutButtonConstraints = new GridBagConstraints();
        aboutButtonConstraints.gridx = 0;
        aboutButtonConstraints.gridy = 4;
        aboutButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(aboutButton, aboutButtonConstraints);

        // Search bar
        JTextField searchBar = new JTextField(35);
        styleSearchBar(searchBar);
        GridBagConstraints searchBarConstraints = new GridBagConstraints();
        searchBarConstraints.gridx = 0;
        searchBarConstraints.gridy = 1;
        searchBarConstraints.insets = new Insets(0, 10, 10, 10);
        buttonSearchPanel.add(searchBar, searchBarConstraints);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(150, 32));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(buttonColor);
        searchButton.setFont(new Font("Arial", Font.BOLD, 20));
        GridBagConstraints searchButtonConstraints = new GridBagConstraints();
        searchButtonConstraints.gridx = 1;
        searchButtonConstraints.gridy = 1;
        searchButtonConstraints.insets = new Insets(5, 0, 10, 10);
        buttonSearchPanel.add(searchButton, searchButtonConstraints);

        // Home button
        JButton homeButton = new JButton("Home");
        styleButton(homeButton,buttonColor, Color.WHITE);
        GridBagConstraints homeButtonConstraints = new GridBagConstraints();
        homeButtonConstraints.gridx = 0;
        homeButtonConstraints.gridy = 5;
        homeButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(homeButton, homeButtonConstraints);

        // Action listeners for buttons
        countriesButton.addActionListener(e -> displayCountries());
        coursesButton.addActionListener(e -> displayCourses());
        aboutButton.addActionListener(e -> displayAbout());
        searchButton.addActionListener(e -> searchDatabase(searchBar.getText()));
        homeButton.addActionListener(e -> backHome());

        // Add action listener to search bar for Enter key press
        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDatabase(searchBar.getText());
            }
        });

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    private static void styleButton(JButton button, Color background, Color foreground) {
        button.setPreferredSize(new Dimension(250, 40));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(new Font("Arial", Font.BOLD, 20));
    }

    private static void styleSearchBar(JTextField searchBar) {
        searchBar.setPreferredSize(new Dimension(200, 30));
        searchBar.setFont(new Font("Calibri", Font.BOLD, 18));
    }
    
    //to clear the panel for adding other components
    public static void clearPanel() {
        frame.getContentPane().removeAll();
        frame.repaint();
    }

    //to regenerate the home page
    public static void backHome() {
        frame.getContentPane().removeAll(); // Remove all components from the frame's content pane
        
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon backgroundImageIcon = new ImageIcon("path_to_img");
                Image backgroundImage = backgroundImageIcon.getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        Color buttonColor = Color.decode("#F39016");

        // Heading panel with GridBagLayout
        JPanel headingPanel = new JPanel(new GridBagLayout());
        headingPanel.setOpaque(false);
        GridBagConstraints headingPanelConstraints = new GridBagConstraints();
        headingPanelConstraints.gridx = 0;
        headingPanelConstraints.gridy = 0;
        headingPanelConstraints.anchor = GridBagConstraints.NORTHWEST; // Align to top left corner
        panel.add(headingPanel, BorderLayout.NORTH); // Add to the NORTH position

        // Heading label
        JLabel headingLabel = new JLabel("STUDY ABROAD INFORMATION SYSTEM");
        headingLabel.setFont(new Font("Times New Roman",Font.BOLD, 50));
        headingLabel.setForeground(Color.WHITE);
        GridBagConstraints headingLabelConstraints = new GridBagConstraints();
        headingLabelConstraints.insets = new Insets(20, 0, 0, 0);
        headingPanel.add(headingLabel,headingLabelConstraints);

        // Button and search bar panel
        JPanel buttonSearchPanel = new JPanel(new GridBagLayout());
        buttonSearchPanel.setOpaque(false);
        GridBagConstraints buttonSearchPanelConstraints = new GridBagConstraints();
        buttonSearchPanelConstraints.gridx = 0;
        buttonSearchPanelConstraints.gridy = 0;
        buttonSearchPanelConstraints.anchor = GridBagConstraints.WEST; // Align to the left
        panel.add(buttonSearchPanel, BorderLayout.WEST); // Add to the WEST position

        // Countries button
        JButton countriesButton = new JButton("Countries");
        styleButton(countriesButton, buttonColor, Color.WHITE);
        GridBagConstraints countriesButtonConstraints = new GridBagConstraints();
        countriesButtonConstraints.gridx = 0;
        countriesButtonConstraints.gridy = 2;
        countriesButtonConstraints.insets = new Insets(10, 10, 10, 10);
        buttonSearchPanel.add(countriesButton, countriesButtonConstraints);

        // Courses button
        JButton coursesButton = new JButton("Courses");
        styleButton(coursesButton,buttonColor, Color.WHITE);
        GridBagConstraints coursesButtonConstraints = new GridBagConstraints();
        coursesButtonConstraints.gridx = 0;
        coursesButtonConstraints.gridy = 3;
        coursesButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(coursesButton, coursesButtonConstraints);

        // About button
        JButton aboutButton = new JButton("About");
        styleButton(aboutButton, buttonColor, Color.WHITE);
        GridBagConstraints aboutButtonConstraints = new GridBagConstraints();
        aboutButtonConstraints.gridx = 0;
        aboutButtonConstraints.gridy = 4;
        aboutButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(aboutButton, aboutButtonConstraints);

        // Search bar
        JTextField searchBar = new JTextField(35);
        styleSearchBar(searchBar);
        GridBagConstraints searchBarConstraints = new GridBagConstraints();
        searchBarConstraints.gridx = 0;
        searchBarConstraints.gridy = 1;
        searchBarConstraints.insets = new Insets(0, 10, 10, 10);
        buttonSearchPanel.add(searchBar, searchBarConstraints);

        // Search button
        JButton searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(150, 32));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(buttonColor);
        searchButton.setFont(new Font("Arial", Font.BOLD, 20));
        GridBagConstraints searchButtonConstraints = new GridBagConstraints();
        searchButtonConstraints.gridx = 1;
        searchButtonConstraints.gridy = 1;
        searchButtonConstraints.insets = new Insets(5, 0, 10, 10);
        buttonSearchPanel.add(searchButton, searchButtonConstraints);

        // Home button
        JButton homeButton = new JButton("Home");
        styleButton(homeButton,buttonColor, Color.WHITE);
        GridBagConstraints homeButtonConstraints = new GridBagConstraints();
        homeButtonConstraints.gridx = 0;
        homeButtonConstraints.gridy = 5;
        homeButtonConstraints.insets = new Insets(5, 10, 10, 10);
        buttonSearchPanel.add(homeButton, homeButtonConstraints);

        // Action listeners for buttons
        countriesButton.addActionListener(e -> displayCountries());
        coursesButton.addActionListener(e -> displayCourses());
        aboutButton.addActionListener(e -> displayAbout());
        searchButton.addActionListener(e -> searchDatabase(searchBar.getText()));
        homeButton.addActionListener(e -> backHome());

        // Add action listener to search bar for Enter key press
        searchBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchDatabase(searchBar.getText());
            }
        });

        frame.setContentPane(panel);
        frame.revalidate(); // Revalidate the frame to update the changes
    }

    //to display information retrieved from database in table form
    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int i = 1; i <= columnCount; i++) {
            columnNames[i - 1] = metaData.getColumnName(i);
        }
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            tableModel.addRow(row);
        }
        return tableModel;
    }

    //display all countries in database
    public static void displayCountries() {
    clearPanel();
    try {
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
        String query = "SELECT DISTINCT Country FROM studyabroad";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        JTable table = new JTable(buildTableModel(rs));

        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));

        JScrollPane scrollPane = new JScrollPane(table);

        // Create a container panel with vertical BoxLayout
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
    	containerPanel.add(scrollPane, BorderLayout.CENTER);
    	containerPanel.add(homeButton, BorderLayout.SOUTH);
        // Set the container panel as the frame's content pane
        frame.setContentPane(containerPanel);
	    homeButton.addActionListener(e -> backHome());
	    frame.setVisible(true);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                String country = (String) table.getValueAt(row, 0);
                displayUniversitiesByCountry(country);
            }
        });
        conn.close();
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    }

    //display universities by country
    public static void displayUniversitiesByCountry(String country) {
        clearPanel();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT DISTINCT UniversityName FROM studyabroad WHERE Country=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, country);
            ResultSet rs = stmt.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
		    JButton homeButton = new JButton("Home");
            homeButton.setFont(new Font("Arial", Font.BOLD, 20));
            JScrollPane scrollPane = new JScrollPane(table);
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BorderLayout());
            containerPanel.add(scrollPane, BorderLayout.CENTER);
            containerPanel.add(homeButton, BorderLayout.SOUTH);
            // Set the container panel as the frame's content pane
            frame.setContentPane(containerPanel);
	        homeButton.addActionListener(e -> backHome());
	        frame.setVisible(true);
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    String university = (String) table.getValueAt(row, 0);
                    displayUniversityDetails(university);
                }
            });
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //display all courses
    public static void displayCourses() {
        clearPanel();
        String[] subjects = {"Computer science", "Technology", "Engineering", "Architecture", "Business", "Management", "Economics", "Psychology", "Medicine", "Sciences", "Humanities", "Political Science", "International Affairs", "Law", "Environmental Science", "Natural Sciences", "Veterinary Medicine","Health Science", "Dentistry", "War Studies", "Finance", "Social Science", "Mathematics", "Nursing", "Theology", "Agriculture", "Physics", "Chemistry", "Biology", "Arts", "Journalism"};

        // Creating table with the subjects
        String[] columnNames = {"Subjects"};
        Object[][] data = new Object[subjects.length][1];
        for (int i = 0; i < subjects.length; i++) { data[i][0] = subjects[i];}
        JTable table = new JTable(data, columnNames);
        JButton homeButton = new JButton("Home");
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        containerPanel.add(homeButton, BorderLayout.SOUTH);
        frame.setContentPane(containerPanel);
        homeButton.addActionListener(e -> backHome());
        frame.setVisible(true);
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                String course = (String) table.getValueAt(row, 0);
                displayUniversitiesByCourse(course);
            }
        });
    }

    //display universities by course
    public static void displayUniversitiesByCourse(String course) {
        clearPanel();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT DISTINCT UniversityName FROM studyabroad WHERE BestCourses like ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + course + "%");
            ResultSet rs = stmt.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JButton homeButton = new JButton("Home");
            homeButton.setFont(new Font("Arial", Font.BOLD, 20));
            JScrollPane scrollPane = new JScrollPane(table);
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BorderLayout());
            containerPanel.add(scrollPane, BorderLayout.CENTER);
            containerPanel.add(homeButton, BorderLayout.SOUTH);
            frame.setContentPane(containerPanel);
            homeButton.addActionListener(e -> backHome());
        frame.setVisible(true);
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    String university = (String) table.getValueAt(row, 0);
                    displayUniversityDetails(university);
                }
            });
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    //display details of a university
    public static void displayUniversityDetails(String university) {
        clearPanel();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT * FROM studyabroad WHERE UniversityName = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, university);
            ResultSet rs = stmt.executeQuery();

            // Retrieve the university details from the result set
            if (rs.next()) {
                String universityName = rs.getString("UniversityName");
                String country = rs.getString("Country");
                String requirements = rs.getString("Requirements");
                String acceptanceRate = rs.getString("AcceptanceRate");
                String tuitionFees = rs.getString("TutuionFee");
                String bestCourses = rs.getString("BestCourses");
                String averageSalary = rs.getString("AverageSalary");
                String universityLinks = rs.getString("UniversityLinks");

                // Create the labels to display the university details
                JLabel nameLabel = new JLabel("<html><h1 style='font-family: Georgia; font-size: 50px;'>" + universityName + "</h1><hr><hr></html>");
                nameLabel.setForeground(Color.BLACK); // Set label color
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER); // Set alignment

                JLabel countryLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Country: " + country + "</div><br><hr></html>");
                JLabel requirementsLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Requirements: " + requirements + "</div><br><hr></html>");
                JLabel acceptanceRateLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Acceptance Rate: " + acceptanceRate + "</div><br><hr></html>");
                JLabel tuitionFeesLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Tuition Fees: " + tuitionFees + "</div><br><hr></html>");
                JLabel bestCoursesLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Best Courses: " + bestCourses + "</div><br><hr></html>");
                JLabel averageSalaryLabel = new JLabel("<html><div style='text-align: center; font-family: Calibri; font-size: 20px;'>Average Salary: " + averageSalary + "</div><br><hr></html>");

                //to Create the clickable link for University Links
                JLabel universityLinksLabel = new JLabel("<html><div style='font-family: Arial; font-size: 20px;'><a href='" + universityLinks + "'>" + universityLinks + "</a></div><br><hr></html>");
                universityLinksLabel.setForeground(Color.BLUE); // Set link color
                universityLinksLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                universityLinksLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            Desktop.getDesktop().browse(new URI(universityLinks));
                        } catch (IOException | URISyntaxException ex) {
                            ex.printStackTrace();
                        }
                    }
                });


                countryLabel.setHorizontalAlignment(SwingConstants.CENTER);
                requirementsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                acceptanceRateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                tuitionFeesLabel.setHorizontalAlignment(SwingConstants.CENTER);
                bestCoursesLabel.setHorizontalAlignment(SwingConstants.CENTER);
                averageSalaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
                universityLinksLabel.setHorizontalAlignment(SwingConstants.CENTER);

                // Create the panel to hold the labels
                JPanel detailsPanel = new JPanel();
                detailsPanel.setBackground(Color.WHITE); // Set panel background color
                detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
                detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
                detailsPanel.add(nameLabel);
                detailsPanel.add(countryLabel);
                detailsPanel.add(requirementsLabel);
                detailsPanel.add(acceptanceRateLabel);
                detailsPanel.add(bestCoursesLabel);
                detailsPanel.add(tuitionFeesLabel);
                detailsPanel.add(averageSalaryLabel);
                detailsPanel.add(universityLinksLabel);

                // Create the home button
                JButton homeButton = new JButton("Home");
                homeButton.setFont(new Font("Arial", Font.BOLD, 20));
                homeButton.addActionListener(e -> backHome());

                // Create the container panel and add the details panel and home button
                JPanel containerPanel = new JPanel();
                containerPanel.setLayout(new BorderLayout());
                containerPanel.setBackground(Color.WHITE); // Set container panel background color
                containerPanel.add(detailsPanel, BorderLayout.CENTER);
                containerPanel.add(homeButton, BorderLayout.SOUTH);

                frame.setContentPane(containerPanel);
                frame.setVisible(true);
            }

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //to display about message
    public static void displayAbout() {
        clearPanel();
        JLabel aboutLabel = new JLabel("<html><h1 style='text-align: center; font-family: Calibri; font-size: 20px;'>Study Abroad Information System :</h1><hr><h2>[ Version: 1.0 ]</h2><hr></html>");
        JLabel paragraphLabel = new JLabel("<html><div style='text-align: center; font-family: Times New Roman; font-size: 20px; width: 95%;'>" +
                "The Study Abroad Information System is a software application designed to provide information and facilitate access to study abroad opportunities for students. It is a user-friendly system that allows students to explore various countries and universities, as well as search for specific courses of interest. The system offers a visually appealing graphical user interface, with features such as buttons for accessing information about countries, courses, and general information. It also includes a search bar for quick and convenient searching. The system aims to simplify the process of finding study abroad programs, helping students make informed decisions about their education and future opportunities." +
                "</div><br><br><hr></html>");
        JButton homeButton = new JButton("Home");
        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(aboutLabel, BorderLayout.NORTH);
        containerPanel.add(paragraphLabel, BorderLayout.CENTER);
        containerPanel.add(homeButton, BorderLayout.SOUTH);
        containerPanel.setBackground(Color.WHITE);
        frame.setContentPane(containerPanel);
        homeButton.addActionListener(e -> backHome());
        homeButton.setFont(new Font("Arial", Font.BOLD, 20));
        frame.setVisible(true);
    }
    public static void searchDatabase(String detail) {
        clearPanel();
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT UniversityName,Country FROM StudyAbroad WHERE UniversityName LIKE ? OR Requirements LIKE ? OR Country LIKE ? OR BestCourses LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + detail + "%");
            stmt.setString(2, "%" + detail + "%");
            stmt.setString(3, "%" + detail + "%");
            stmt.setString(4, "%" + detail + "%");
            ResultSet rs = stmt.executeQuery();
            JTable table = new JTable(buildTableModel(rs));
            JButton homeButton = new JButton("Home");
            homeButton.setFont(new Font("Arial", Font.BOLD, 20));
            JScrollPane scrollPane = new JScrollPane(table);
            JPanel containerPanel = new JPanel();

            // Add the scroll pane and home button to the container panel
            containerPanel.setLayout(new BorderLayout());
            containerPanel.add(scrollPane, BorderLayout.CENTER);
            containerPanel.add(homeButton, BorderLayout.SOUTH);
            // Set the container panel as the frame's content pane
            frame.setContentPane(containerPanel);
            homeButton.addActionListener(e -> backHome());
            frame.setVisible(true);
            table.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    int row = table.rowAtPoint(evt.getPoint());
                    String university = (String) table.getValueAt(row, 0);
                    displayUniversityDetails(university);
                }
            });
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}