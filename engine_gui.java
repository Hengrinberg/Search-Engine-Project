package doc_parser;

import java.awt.EventQueue;


import java.io.File;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;

public class engine_gui {
	
	public String corpus_path;
	public String output_path;

	private JFrame frame;
	private JTextField search_textField;
	private JTextField txtSelectCorpusDirectory;
	private JTextField txtSelectOutputDirectory;
	private JButton output_folder_btn;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					engine_gui window = new engine_gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public engine_gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent search_line) {
			}
		});
		btnSearch.setBounds(26, 215, 89, 23);
		frame.getContentPane().add(btnSearch);
		
		search_textField = new JTextField();
		search_textField.setBounds(26, 184, 341, 20);
		frame.getContentPane().add(search_textField);
		search_textField.setColumns(10);
		
		JButton btnNewButton = new JButton("browse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent inserted_path) {
				final JLabel label = new JLabel();
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(frame);
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               corpus_path = file.getPath();
	               System.out.printf("chosen corpus directory : %s\n", file.getPath());
//	               label.setText("Folder Selected: " + file.getName());
	            }else{
//	               label.setText("Open command canceled");
	            }
			}
		});
		btnNewButton.setBounds(26, 42, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		txtSelectCorpusDirectory = new JTextField();
		txtSelectCorpusDirectory.setText("select corpus directory");
		txtSelectCorpusDirectory.setBounds(26, 11, 157, 20);
		frame.getContentPane().add(txtSelectCorpusDirectory);
		txtSelectCorpusDirectory.setColumns(10);
		
		txtSelectOutputDirectory = new JTextField();
		txtSelectOutputDirectory.setText("select output directory");
		txtSelectOutputDirectory.setColumns(10);
		txtSelectOutputDirectory.setBounds(26, 98, 157, 20);
		frame.getContentPane().add(txtSelectOutputDirectory);
		
		output_folder_btn = new JButton("browse");
		output_folder_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JLabel label = new JLabel();
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(frame);
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               output_path = file.getPath();
	               System.out.printf("chosen output directory : %s\n", file.getPath());
//	               label.setText("Folder Selected: " + file.getName());
	            }else{
//	               label.setText("Open command canceled");
	            }
			}
		});
		output_folder_btn.setBounds(26, 129, 89, 23);
		frame.getContentPane().add(output_folder_btn);
		
		JButton btnParseThisShit = new JButton("parse this shit?");
		btnParseThisShit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debugger d = new Debugger(corpus_path, output_path);
			}
		});
		btnParseThisShit.setBounds(218, 67, 149, 23);
		frame.getContentPane().add(btnParseThisShit);
	}
}
