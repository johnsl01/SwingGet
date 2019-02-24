/*
 * Really basic http GET with a swing interface
 */


package swingget;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.*;

// SWING IMPORTS
import javax.swing.JFrame ;
import javax.swing.GroupLayout;
import javax.swing.JLabel ;
import javax.swing.JButton ;
import javax.swing.JTextField ;
import javax.swing.JTextArea ;
import javax.swing.JScrollPane ;
import javax.swing.JComponent ;


public class SwingGet 
            extends JFrame 
            implements ActionListener           
{

	// class constants
    private static final long serialVersionUID = 1L;   
    
    private final String USER_AGENT = "Mozilla/5.0";
    
    public static final String TITLE = "Swing GET (V 1.00 21/02/2019)" ;
    
    public static final String NEWLINE = "\n";
    
    // class variables
    static boolean DEBUG = true ;
    static Integer DEBUGLEVEL = 9 ;
       
    static String page ;
    static String url;
    
    // gui components - all within the class's JFrame
    static JLabel urlLabel ;    
    static JTextField urlText ;
    
    static JButton goButton ;
    static String goButtonText = "GO" ;
    
    static JButton clearTextButton ;
    static String clearTextButtonText = "Clear Results" ;
    
    static JTextArea reportArea ;
    static JScrollPane reportScrollPane ;
  
    
    // Class constructor
    public SwingGet()
    {
        initUI();
        println(9,"In SwingGet Class Constructor");
    } // SwingGet()
 
    
    private void initUI()
    {
        urlLabel = new JLabel ( "URL : " ) ;
	    urlText = new JTextField ( 60 ) ;
        goButton = new JButton ( goButtonText ) ;
        clearTextButton = new JButton ( clearTextButtonText ) ;
        reportArea = new JTextArea( "Output : \n", 20, 0  ) ;
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportScrollPane = new JScrollPane(reportArea);
        
        createLayout (
                    urlLabel, // item 0  
                    urlText,
                    goButton,
                    clearTextButton,
                    reportScrollPane );
        
        setTitle ( TITLE );
        setLocationRelativeTo ( null ) ;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    
        urlText.setText ( "http://www.irishcoinage.com/J01269.HTM" ) ;
        
        goButton.addActionListener(this);
        clearTextButton.addActionListener(this);        
    } // initUI()  
    
    
    private void createLayout (JComponent... arg)
    {
        Container pane = getContentPane();		
        GroupLayout gl = new GroupLayout ( pane ) ;		
        pane.setLayout( gl );
        
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        
        gl.setHorizontalGroup (
            gl.createParallelGroup() 
                .addGroup(gl.createSequentialGroup()
                    .addComponent(arg[0])
                    .addComponent(arg[1]) 
                )
                .addGroup(gl.createSequentialGroup()
                    .addComponent(arg[2])
                    .addComponent(arg[3]) 
                )
                .addComponent(arg[4]) 
        );
        gl.setVerticalGroup (
            gl.createSequentialGroup() 
                .addGroup(gl.createParallelGroup()
                    .addComponent(arg[0])
                    .addComponent(arg[1]) 
                )
                .addGroup(gl.createParallelGroup()
                    .addComponent(arg[2])
                    .addComponent(arg[3]) 
                )
                .addComponent(arg[4]) 
        );        
        gl.linkSize(arg[1]);		
	    pack();        
    }  // createLayout ()      
    
    
    public void actionPerformed ( ActionEvent e )			
    {
        String actionCommandText ;
        actionCommandText = e.getActionCommand() ;
        
        println( 3, "In actionPerformed ....." ) ;
        println( 3, "Action Command Text :>" + actionCommandText + "<") ;
        
        
        if ( actionCommandText.equals( goButtonText ) )
	{
            println( 3, "Go Button Pressed .... " ) ;
            try 
            {
                url = urlText.getText();
                println( 0, url ) ;
                page = getPage(url);
                println( 0, "Content : " + NEWLINE + page + NEWLINE ) ;
            }
            catch (Exception f)
            {
                println( 0, "oops .. failed to get page \n" ) ;
            }
            }
        else if ( actionCommandText.equals( clearTextButtonText ) )
        {
            // println( 3, "Clear Inputs Button Pressed .... " ) ;
            reportArea.setText( "Output (reset) : \n" );
        }
        else 
        {
            println( 0, "unidentified action requested >" + actionCommandText + "<" ) ;
        }         
    } // actionPerformed ()
      
    
    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) 
    {                
        EventQueue.invokeLater
        ( () -> 
            {
                SwingGet gui = new SwingGet();
                gui.setVisible(true);
            }
        );	                
    } // main ()

    
    public static void println ( int level, String message )
    {		
        if ( (DEBUG) & (level <= DEBUGLEVEL) )
        {
            // Standard Output :		
            // System.out.println ( message ) ;

            // Text Area Output : 
            reportArea.append ( NEWLINE + message ) ;
            // Make sure the newest text is visible
            reportArea.setCaretPosition(reportArea.getDocument().getLength());

            // attempt to update report area after each message is appended
            // this isn't working - why not ?
            reportArea.repaint() ;	
        }
    } // println ()    
    

/**
     * 
     * @param url - String : full URL 
     * @return String : unmodified page contents
     * @throws Exception - not handled
     */
    private String getPage ( String url ) 
                            throws Exception
    {
    	int responseCode ;
    	String responseMessage ;
    	String headerFieldkey ;
    	String headerField ;
    	
    	URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection() ;

        // optional default is GET
        con.setRequestMethod("GET") ;

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT) ;
               
        println(1, "\nSending 'GET' request to URL : " + url + NEWLINE) ;
        responseCode = con.getResponseCode() ;
        println(1, "Response Code : " + responseCode) ;
        responseMessage = con.getResponseMessage() ;
        println(1, "Response Message : " + responseMessage + NEWLINE) ;
        
        for (int i=1; i<12; i++) 
        {
        	headerFieldkey = con.getHeaderFieldKey(i) ;
        	headerField = con.getHeaderField(i) ;        	
        	println(1, "Header Field " + i + " : " + headerFieldkey + " : " + headerField) ;
        }
        println(1, "") ;

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()) ) ;
        String inputLine;
        StringBuffer response = new StringBuffer() ;

        while ((inputLine = in.readLine()) != null) 
        {
            // output individual lines
            // println(inputLine) ;
            response.append(inputLine) ;
            response.append(NEWLINE) ;
        }
        in.close() ;			

        return response.toString() ;
    } /* String getPage ( String url ) */
	 
} // Class SwingGet

