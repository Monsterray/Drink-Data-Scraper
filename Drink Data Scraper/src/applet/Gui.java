/**
 * 
 */
package applet;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import objects.DrinkDatabase;

/**
 * @author Monsterray
 *
 */
public class Gui extends Applet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4027746924189020457L;
	private AppletListener listen = new AppletListener(this);
	private boolean drawTabs;
	private JTabbedPane tabPanel;
	private JFrame mainFrame;
	private String frameTitle;
	private JTextArea textArea;
	private JTextArea messagesArea;
	private JMenuBar topMenuBar;
	private JScrollPane scrollArea;
	JTextField searchField;
	private ImageIcon searchIcon = createImageIcon("images/command.gif");
	private ImageIcon aboutIcon = createImageIcon("images/about.png");
	
	private Dimension appletDimensions = new Dimension(765, 503);
	private Toolkit toolkit;
	private Dimension screenSize;
	private int screenWidth;
	private int screenHeight;
	@SuppressWarnings("unused")
	private int dialogSelectionType;
	
	@SuppressWarnings("unused")
	private DrinkDatabase drinkData;
	
	
	/**
	 * @param args
	 */
	public Gui(String[] args){
//		System.out.println(System.getProperty("java.class.path"));
		List<String> argList = Arrays.asList(args);
		System.out.println("[INFO] Gui called");
		drawTabs = argList.contains("-tabbed") || argList.contains("-both");
		System.out.println("[INFO] Drawing tabs: " + drawTabs);
//		try {
//			UIManager.setLookAndFeel("org.jvnet.substance.skin.SubstanceEmeraldDuskLookAndFeel");
//		} catch (ClassNotFoundException e1) {
//			e1.printStackTrace();
//		} catch (InstantiationException e1) {
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			e1.printStackTrace();
//		} catch (UnsupportedLookAndFeelException e1) {
//			e1.printStackTrace();
//		}
		try {
			initUI();
		} catch (Exception e) {
			System.out.println("FAIL: " + e.toString());
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void initUI() {
		System.out.println("[INFO] Initializing...");
		tabPanel = new JTabbedPane();
		mainFrame = new JFrame(frameTitle);
		setCornerIcon("assets/images/advisor 0.png");
		
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		tabPanel.putClientProperty("substancelaf.tabbedpanehasclosebuttons", true);

		mainFrame.setLayout(new BorderLayout());
		mainFrame.setResizable(true);
		mainFrame.setPreferredSize(appletDimensions);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().removeAll();
		centerWindow();
		

		messagesArea = new JTextArea(20, 30);
		messagesArea.setFont(new Font("Arial Bold", 0, 14));
		messagesArea.setRows(7);
		messagesArea.setForeground(new Color(100, 100, 255));
		messagesArea.setBackground(new Color(0, 0, 0));
		
		scrollArea = new JScrollPane(this.messagesArea, 22, 31);
		scrollArea.setPreferredSize(new Dimension(750, 450));
		scrollArea.setVerticalScrollBarPolicy(22);
		scrollArea.setHorizontalScrollBarPolicy(32);
		
		
		textArea = new JTextArea();
		textArea.setBackground(new Color(166, 166, 166));
		textArea = readTabInfo("../Version.txt", textArea);
		JScrollPane scrollTextArea = new JScrollPane(textArea);
		
		topMenuBar = addMenuBar(new JMenuBar());	// Leaving it this way so I can change the toolbar later in the program
		mainFrame.getContentPane().add(topMenuBar, BorderLayout.NORTH);
		
		if (drawTabs) {
			mainFrame.getContentPane().add(tabPanel, BorderLayout.CENTER);
			
			tabPanel.addTab("Search", searchIcon, scrollArea, "Search results go here");
			tabPanel.addTab("Version info", aboutIcon, scrollTextArea, "Version Info");
			
//			tabPanel.setMnemonicAt(0, 49);
		}else{
			mainFrame.getContentPane().add(scrollArea, BorderLayout.CENTER);
		}

		mainFrame.pack();
		mainFrame.setVisible(true);
		mainFrame.getContentPane().addContainerListener(
				new ContainerListener() {
					@Override
					public void componentAdded(ContainerEvent e) {
						mainFrame.pack();
					}

					@Override
					public void componentRemoved(ContainerEvent e) {
						mainFrame.pack();
					}
				});

//		init();
	}

	/**
	 * 
	 */
	public void readData(){
		drinkData = new DrinkDatabase("../Data Drinks/");
	}

	/**
	 * Used to find search results with the line taken in form the search field
	 */
	public void findSearchResults() {
		String searchRegex = searchField.getText();
		System.out.println(searchRegex + " Was in the searchField");
		
		
	}

	/**
	 * Centers the applet in the center of the center
	 */
	private void centerWindow() {
		toolkit = Toolkit.getDefaultToolkit();
		screenSize = toolkit.getScreenSize();
		screenWidth = (int) screenSize.getWidth();
		screenHeight = (int) screenSize.getHeight();
		mainFrame.setLocation((screenWidth - (int) appletDimensions.getWidth()) / 2, (screenHeight - (int) appletDimensions.getHeight()) / 2);
	}

	/**
	 * @param menuBar
	 * @return
	 */
	private JMenuBar addMenuBar(JMenuBar menuBar) {
		menuBar.add(createButtonTab("File", new String[] { "Change Path",
				"Save Screenshot", "-", "Vote", "Donate", "Forums", "-",
				"Item List", "World Map", "Object IDs", "-", "Exit" }));
 		menuBar.add(createButtonTab("Testing", new String[] {"Display Classes"}));
//		menuBar.add(createButtonTab("Themes", getThemes("./theme/Theme.jar", "org.jvnet.substance.skin")));
		menuBar.add(createButton("Screenshot", "Screenshot"));
		menuBar.add(createButton("||>", "pause"));
		menuBar.add(createButton(" Search ", "searchGo"));
		searchField = new JTextField();
//		searchField.setPreferredSize(new Dimension(500, 15));
		menuBar.add(searchField);
		return menuBar;
	}

	/**
	 * @param buttonName
	 * @param buttonCommandName
	 * @return
	 */
	public JButton createButton(String buttonName, String buttonCommandName) {
		JButton button = new JButton(buttonName);
		button.setActionCommand(buttonCommandName);
		button.addActionListener(listen);
		return button;
	}

	/**
	 * @param tabName
	 * @param subTabNames
	 * @return
	 */
	public JMenu createButtonTab(String tabName, String[] subTabNames) {
		JMenu Menu;
		try {
			Menu = new JMenu(tabName);
			for (String name : subTabNames) {
				JMenuItem menuItem = new JMenuItem(name);
				if (name.equalsIgnoreCase("-")) {
					Menu.addSeparator();
				} else {
					menuItem.addActionListener(listen);
					Menu.add(menuItem);
				}
			}
			return Menu;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param comp
	 * @param title
	 * @param container
	 * @return
	 */
	public JDialog createDialog(Component comp, String title, Container container) {
		JDialog jdialog = new JDialog(mainFrame, title, true);
		jdialog.setDefaultCloseOperation(2);
		jdialog.add(comp);
		jdialog.pack();
		jdialog.setLocationRelativeTo(container);
		return jdialog;
	}

	/**
	 * @param defaultPath
	 * @param dialogType
	 * @return
	 */
	public JFileChooser createFileChooser(String defaultPath, int dialogType) {
		JFileChooser fChooser;
		try {
			fChooser = new JFileChooser(); // creates a JFileChooser
			fChooser.setFileSelectionMode(0); // either files(0) or folders(1)
			fChooser.addChoosableFileFilter(new ImageFileFilter()); // sets the filter in the drop down menu bar for extension types
			fChooser.setCurrentDirectory(new File(defaultPath)); // sets the directory the chooser begins in
			String title = ((int) (Math.random() * 100)) + ".png"; // gets a random name for the file, will change this later
			fChooser.setSelectedFile(new File(title)); // sets the
			fChooser.setDialogType(dialogType); // either opening(0) or saveing(1)
			dialogSelectionType = dialogType; // sets my variable to tell if the user is loading or saveing
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[Failed] to create FileChooser");
			return null;
		}
		return fChooser;
	}

	/**
	 * @param path
	 * @param jta
	 * @return
	 */
	public JTextArea readTabInfo(String path, JTextArea jta) {
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader(path));
			jta.read(objectFile, null);
			return jta;
		} catch (FileNotFoundException fileex) {
			fileex.printStackTrace();
			System.out.println(path + ": not found.");
			return null;
		} catch (NullPointerException nullE) {
			nullE.printStackTrace();
			System.out.println(path + ": is null");
			return null;
		} catch (IOException ioe) {
			System.out.println("IO Error: " + path);
			return null;
		}
	}

	/**
	 * @param path
	 */
	public void setCornerIcon(String path) {
		try {
			if ((new File(path)).isFile()) {
				mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(path));
			} else {
				System.out.println("Can't Find the icon at: " + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Can't Find the icon at: " + path);
		}
	}

	/**
	 * @param path
	 * @return
	 */
	protected ImageIcon createImageIcon(String path) {
		try {
			// URL imgURL = Gui.class.getResource(path);
			if (path != null) {
				return new ImageIcon(path);
			} else {
				System.err.println("Couldn't find file: " + path);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Couldn't find file: " + path);
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Gui(args);
	}

//	/**
//	 * @param cmd
//	 * @param fileDir
//	 * @param songName
//	 */
//	public void musicPlayer(String cmd, String fileDir, String songName) {
//		// String fileDir = FileDIR;// signlink.findcachedir() +"Mp3/Music/";
//		if (player != null && player.isRunning()) {
//			player.close();
//		}
//		if (sequencer != null && sequencer.isRunning()) {
//			sequencer.stop();
//			sequencer.close();
//		}
//		if (cmd.equals("Next")) {
//			midiCount++;
//		} else if (cmd.equals("Previous")) {
//			midiCount--;
//		} else if (!cmd.equals("Stop")) {
//			File file;
//			try{
//				file = new File(fileDir);
//			}catch(Exception e){
//				file = null;
//			}
//			String songList[] = file.list();
//			if (songName != null) {
//				for (int i = 0; i <= songList.length - 1; i++) {
//					if (songList[i].equals(songName)) {
//						midiCount = i;
//						break;
//					}
//				}
//			}
//			if (songList.length > 0) {
//				if (midiCount > songList.length - 1) {
//					midiCount = 0;
//				}
//				if (midiCount < 0) {
//					midiCount = songList.length - 1;
//				}
//				if (songList[midiCount].endsWith(".mp3")) {
//					System.err.println("Playing " + songList[midiCount]);
//					player = new MP3Player(fileDir + songList[midiCount], true);
//				} else if (songList[midiCount].endsWith(".mid")) {
//					System.err.println("Playing " + songList[midiCount]);
//					try {
//						File file1 = new File(fileDir + songList[midiCount]);
//						sequencer = MidiSystem.getSequencer();
//						sequencer.setSequence(MidiSystem.getSequence(file1));
//						if (sequencer.getMicrosecondLength() <= 5000000) {
//							midiCount++;
//							file1 = new File(fileDir + songList[midiCount]);
//							sequencer = MidiSystem.getSequencer();
//							sequencer.setSequence(MidiSystem.getSequence(file1));
//						}
//						sequencer.setLoopCount(1);
//						sequencer.open();
//						sequencer.start();
//					} catch (MidiUnavailableException midiunavailableexception) {
//						System.err.println("[WARNING] Midi device unavailable!");
//					} catch (InvalidMidiDataException invalidmididataexception) {
//						System.err.println("[WARNING] Invalid Midi data!");
//					} catch (IOException ioexception) {
//						System.err.println("[WARNING] I/O Error!");
//					}
//				} else if (songList[midiCount].endsWith(".wma")) {
//					System.err.println("[INFO] Sorry .wma files are not suported");
//				} else {
//					System.err.println();
//					System.err.println("[DEBUG] ~~~Failed to play item~~~");
//					System.err.println("[DEBUG] midiCount: " + midiCount);
//					System.err.println("[DEBUG] fileDir: " + fileDir);
//					System.err.println("[DEBUG] cmd: " + cmd);
//					System.err.println("[DEBUG] songList.length: "+ songList.length);
//					System.err.println("[DEBUG] song name: "+ songList[midiCount]);
//					System.err.println("[DEBUG] ~~~~~~~~~~~~~~~~~~~~~~~~~");
//				}
//			} else {
//				System.err.println("[WARNING] No midi or mp3 files in folder");
//			}
//		}
//	}

}
