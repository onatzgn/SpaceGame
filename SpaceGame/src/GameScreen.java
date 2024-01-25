import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GameScreen extends JFrame {
    private GamePanel game;
    private JPanel startscreen;
    private boolean isLoggedIn;
    private String usernameT;
    
    private boolean isMenuScreen = true;
    private static final String GAME_SONG_FILE_PATH = "Sounds/gameSong.wav";
    private Clip menuSongClip;
    
    private JPanel highScoresPanel;
    private JPanel registerPanel;
    private JPanel loginPanel;
    private JButton backButton;
	

	public GameScreen(String title) throws HeadlessException {
        super(title);
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JMenuBar menubar = MenuBar();
        setJMenuBar(menubar);
        
        game = new GamePanel();
        game.setBounds(0, 0, 800, 600);
        game.setVisible(false);
        add(game);
        
        game.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                game.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                game.keyReleased(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
                game.keyTyped(e);
            }
        });

        startscreen = new JPanel();
        startscreen.setBounds(0, 0, 800, 600);
        startscreen.setLayout(null);
        startscreen.setBackground(Color.BLACK);
        
        JLabel startLabel = new JLabel(new ImageIcon("Images/start.png"));
        startLabel.setBounds(215, 20, 350, 150);
        startscreen.add(startLabel);
        
        addButtons(startscreen);
        
        add(startscreen);
        
        isLoggedIn = false;

        setVisible(true);
        
        playMenuSong();

	}

	public static void main(String[] args) {
		GameScreen gscreen = new GameScreen("Space Invaders");
		
	}
	
    private JMenuBar MenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // MENU AND MENUITEMS
        JMenu fileMenu = new JMenu("File");
        JMenuItem registerItem = new JMenuItem("Register");
        JMenuItem playGameItem = new JMenuItem("Play Game");
        JMenuItem highScoreItem = new JMenuItem("High Score");
        JMenuItem quitItem = new JMenuItem("Quit");
        
        // REGISTER
        registerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showRegisterDialog();
            }
        });
        
        // PLAY GAME
        playGameItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginDialog();

            }
        });
        
        // HIGH SCORE
        highScoreItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	showHighScores();
            }
        });
        
        // QUIT
        quitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        fileMenu.add(registerItem);
        fileMenu.add(playGameItem);
        fileMenu.add(highScoreItem);
        fileMenu.add(quitItem);

        menuBar.add(fileMenu);
        
        // HELP
        JMenu helpMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About");
        
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "Name: Onat\nSurname: Özgen\nEmail: onat.ozgen@std.yeditepe.edu.tr";
                JOptionPane.showMessageDialog(GameScreen.this, message, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void addButtons(JPanel panel) {
        JButton registerButton = createButton("Register", 200);
        JButton playGameButton = createButton("Play Game", 270);
        JButton highScoreButton = createButton("High Score", 340);
        JButton helpButton = createButton("Help", 410);
        JButton quitButton = createButton("Quit", 480);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showRegisterPanel();
            }
        });

        playGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoginPanel();
            }
        });

        highScoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHighScoresPanel();
            }
        });
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = "W,A,S,D/Arrow Keys/Mouse : Movement\nSpace/Left Mouse Button : Fire\nP : Pause";
                JOptionPane.showMessageDialog(GameScreen.this, message, "Controls", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        backButton = createButton("Back",450);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showMenuScreen();
            }
        });
        
        addMouseListenerToButton(registerButton);
        addMouseListenerToButton(playGameButton);
        addMouseListenerToButton(highScoreButton);
        addMouseListenerToButton(helpButton);
        addMouseListenerToButton(quitButton);
        addMouseListenerToButton(backButton);


        panel.add(registerButton);
        panel.add(playGameButton);
        panel.add(highScoreButton);
        panel.add(helpButton);
        panel.add(quitButton);

    }
    
    private JButton createButton(String buttonText,int y) {
    	
        JButton button = new JButton();

        button.setText(buttonText);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setForeground(Color.BLACK);
        button.setBounds(320, y, 170, 50);
        button.setFocusPainted(false);

        return button;
        
    }
    
    private void addMouseListenerToButton(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 0, 0));
                button.setForeground(Color.GRAY); 
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 255));
                button.setForeground(Color.BLACK); 
            }
        });
    }

	private void showRegisterDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        Dimension panelSize = new Dimension(300, 90);
        panel.setPreferredSize(panelSize);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 80, 25);
        panel.add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);

        int option = JOptionPane.showOptionDialog(null, panel, "Register", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (username != null && password != null) {
                UserManager.registerUser(username, password);
                JOptionPane.showMessageDialog(this, "Registration successful!");
            }
        }
        
    }
    private void showLoginDialog() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        Dimension panelSize = new Dimension(300, 90);
        panel.setPreferredSize(panelSize);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(10, 10, 80, 25);
        panel.add(usernameLabel);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 40, 80, 25);
        panel.add(passwordLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(100, 10, 160, 25);
        panel.add(usernameField);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(100, 40, 160, 25);
        panel.add(passwordField);

        int option = JOptionPane.showOptionDialog(null, panel, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (option == JOptionPane.OK_OPTION) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            
            if (username != null && password != null) {
                 isLoggedIn = UserManager.loginUser(username, password);
                 usernameT = username;
                
                if (isLoggedIn) {
                    JOptionPane.showMessageDialog(this, "Login successful!");
                    startscreen.setVisible(false);
                    game.setVisible(true);
                    game.requestFocus();
                	startGame();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!");
                }
            }
            
        }
    }
    
    private void showHighScores() {
        List<HighScore> highScores = HighScoreManager.getHighScores();
        
        if (highScores.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No high scores available.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("High Scores:\n");
            
            for (int i = 0; i < highScores.size(); i++) {
                HighScore highScore = highScores.get(i);
                sb.append(i + 1).append(". ").append(highScore.getUsername()).append(": ").append(highScore.getScore()).append("\n");
            }
            
            JOptionPane.showMessageDialog(this, sb.toString());
        }
    }
    

    
    private void startGame() {
    	startscreen.setVisible(false);
    	game.setVisible(true);
		game.requestFocus();
		game.addKeyListener(game);
		game.setFocusable(true);
		game.setFocusTraversalKeysEnabled(false);
        if (isLoggedIn) {
            game.startGame(usernameT);
            stopMenuSong();
        } else {
            JOptionPane.showMessageDialog(this, "Please login to start the game.");
        }

    }
    
    private void playMenuSong() {
        if (isMenuScreen) {
            menuSongClip = Sound.playSound(GAME_SONG_FILE_PATH);
        }
    }

    private void stopMenuSong() {
        if (menuSongClip != null && menuSongClip.isRunning()) {
            menuSongClip.stop();
        }
    }
    
    private void showHighScoresPanel() {
        highScoresPanel = new JPanel();
        highScoresPanel.setLayout(null); 
        highScoresPanel.setBackground(Color.BLACK);

        List<HighScore> highScores = HighScoreManager.getHighScores();
        JTextArea highScoresTextArea = new JTextArea();
        highScoresTextArea.setEditable(false);
        highScoresTextArea.setFont(new Font("Arial", Font.PLAIN, 18));

        if (highScores.isEmpty()) {
            highScoresTextArea.setText("No high scores available.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("High Scores:\n");

            for (int i = 0; i < highScores.size(); i++) {
                HighScore highScore = highScores.get(i);
                sb.append(i + 1).append(". ").append(highScore.getUsername()).append(": ").append(highScore.getScore()).append("\n");
            }

            highScoresTextArea.setText(sb.toString());
            highScoresTextArea.setBackground(Color.YELLOW);
        }
        
        int textAreaWidth = 150;  
        int textAreaHeight = 230;  
        int x = (800 - textAreaWidth) / 2;  
        int y = (600 - textAreaHeight) / 2;  
        highScoresTextArea.setBounds(x, y, textAreaWidth, textAreaHeight);

        JScrollPane scrollPane = new JScrollPane(highScoresTextArea);
        scrollPane.setBounds(x, y, textAreaWidth, textAreaHeight);
        highScoresPanel.add(scrollPane);

        backButton.setBounds(720, 10, 70, 30); 
        highScoresPanel.add(backButton);
        backButton.setVisible(true);

        add(highScoresPanel);
        highScoresPanel.setBounds(0, 0, 800, 600);
        highScoresPanel.setVisible(true);
        startscreen.setVisible(false);
    }

    private void showRegisterPanel() {
        registerPanel = new JPanel();
        registerPanel.setLayout(null);
        registerPanel.setBackground(Color.BLACK);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(250, 575/2, 80, 25);
        registerPanel.add(usernameLabel);
        usernameLabel.setForeground(Color.YELLOW);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(320, 575/2, 160, 25);
        registerPanel.add(usernameField);
        usernameField.setBackground(Color.YELLOW);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(250, (575/2)+30, 80, 25);
        registerPanel.add(passwordLabel);
        passwordLabel.setForeground(Color.YELLOW);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(320, (575/2)+30, 160, 25);
        registerPanel.add(passwordField);
        passwordField.setBackground(Color.YELLOW);

        JButton registerButton = new JButton("Register");
        registerButton.setBounds(320, 450, 150, 50);
        registerPanel.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username != null && password != null) {
                    UserManager.registerUser(username, password);
                    JOptionPane.showMessageDialog(GameScreen.this, "Registration successful!");
                }
            }
        });

        add(registerPanel);
        registerPanel.setBounds(0, 0, 800, 600);
        registerPanel.setVisible(true);
        startscreen.setVisible(false);
        
        backButton.setBounds(720, 10, 70, 30); 
        registerPanel.add(backButton);
        backButton.setVisible(true);
    }

    private void showLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(Color.BLACK);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(250, 575/2, 80, 25);
        usernameLabel.setForeground(Color.YELLOW);
        loginPanel.add(usernameLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(320, 575/2, 160, 25);
        usernameField.setBackground(Color.YELLOW);
        loginPanel.add(usernameField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(250, (575/2)+30, 80, 25);
        passwordLabel.setForeground(Color.YELLOW);
        loginPanel.add(passwordLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(320, (575/2)+30, 160, 25);
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(320, 450, 150, 50);
        passwordField.setBackground(Color.YELLOW);
        loginPanel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username != null && password != null) {
                    isLoggedIn = UserManager.loginUser(username, password);
                    usernameT = username;

                    if (isLoggedIn) {
                        JOptionPane.showMessageDialog(GameScreen.this, "Login successful!");
                        startscreen.setVisible(false);
                        game.setVisible(true);
                        game.requestFocus();
                        startGame();
                    } else {
                        JOptionPane.showMessageDialog(GameScreen.this, "Invalid username or password!");
                    }
                }
            }
        });

        add(loginPanel);
        loginPanel.setBounds(0, 0, 800, 600);
        loginPanel.setVisible(true);
        startscreen.setVisible(false);
        
        backButton.setBounds(720, 10, 70, 30); 
        loginPanel.add(backButton);
        backButton.setVisible(true);
    }

    private void showMenuScreen() {
        if (highScoresPanel != null) {
            highScoresPanel.setVisible(false);
        }

        if (registerPanel != null) {
            registerPanel.setVisible(false);
        }

        if (loginPanel != null) {
            loginPanel.setVisible(false);
        }
        
        backButton.setBounds(720, 10, 70, 30); 
        startscreen.setVisible(true);
        backButton.setVisible(false);
    }

   

}


