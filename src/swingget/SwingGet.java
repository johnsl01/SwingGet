/** 
* SwingGet
*
* Really basic http GET with a swing inerface
*
* @author johnsl
*/

package swingget;

// IO imports
import java.io.BufferedReader;
import java.io.InputStreamReader;

// NET imports
import java.net.HttpURLConnection;
import java.net.URL;

// AWT imports
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.*;

// SWING imports
import javax.swing.JFrame ;
import javax.swing.GroupLayout;
import javax.swing.JLabel ;
import javax.swing.JButton ;
import javax.swing.JTextField ;
import javax.swing.JTextArea ;
import javax.swing.JScrollPane ;
import javax.swing.JComponent ;

public class SwingGet 
            extends JFrame   // because it needs to have a Swing GUI interface
            implements ActionListener  // because user gui interaction has to trigger actions
            
{
    // class constants 
    private final String USER_AGENT = "Mozilla/5.0";  // Any browser (or junk text) will do     
    private static final long serialVersionUID = 1L;
    public static final String TITLE = "Swing GET (V 1.00 21/02/2019)" ;
    public static final String NEWLINE = "\n";
    
    // class variables
    static boolean DEBUG = true ;
    static Integer DEBUGLEVEL = 9 ;  // messages to the text area are filtered by this level
       
    static String page ;  // The response content 
    static String url;  // The user supplied url - defaults to http://www.google.com/
    
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
        println(9,"In SwingGet Class Constructor"); // doesnt't work - too early to call it.
    } // SwingGet()
    
    private void initUI()
    {
    	// define the gui components being used
        urlLabel = new JLabel ( "URL : " ) ;
	urlText = new JTextField ( 60 ) ;
        goButton = new JButton ( goButtonText ) ;
        clearTextButton = new JButton ( clearTextButtonText ) ;
	// JTextArea is within a JScrollPane
        reportArea = new JTextArea( "Output : \n", 20, 0  ) ;
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportScrollPane = new JScrollPane(reportArea);
        
	// put the gui components into the layout 
	// note comments to keep track of numbers - becomes important with complex layouts	
        createLayout (
                    urlLabel, // item 0  
                    urlText,
                    goButton,
                    clearTextButton,
                    reportScrollPane // item 4
		    );
        
	// basic window stuff
        setTitle ( TITLE );
        setLocationRelativeTo ( null ) ;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
	// default value to help the user understand what should be entered
        urlText.setText ( "http://www.google.com/" ) ; 
        
	// add the listeners to the components which need to trigger actions
	// note - changing the URL text is not set to trigger anything
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
        
	// this is a really tedious mechanism
	// the horizontal and vertical groups define the same stuff
	// but horizontal is parallel then sequential groups
	// whereas vertical is sequental then parallel groups
	// it becomes very messy for complex gui layouts
	// and arguement numbering makes it easy to mess up
	// there is probably a better way - but this works.
	// The layout we want here is :
	//   0    1   :      urlLabel       urlText
	//   2    3   :      goButton       cleartextButton
	//      4     :           reportSrollPane
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
        gl.linkSize(arg[1])		
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
                println( 0, url + NEWLINE ) ;
                page = getPage(url);
                println( 0, page + NEWLINE + NEWLINE ) ;
            }
            catch (Exception f)
            {
                println( 0, "Oops .. failed to get page \n" ) ;
            }
        }
        else if ( actionCommandText.equals( clearTextButtonText ) )
        {
            // println( 3, "Clear Results Button Pressed .... " ) ; // duh ! 
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
    public static void main(String[] args) 
    {        
        // System.out.println("Hello World");  // console diag message 
        
        EventQueue.invokeLater
        (
            () -> 
            {
                SwingGet gui = new SwingGet();
                gui.setVisible(true);
            }
        );	       
    } // main()
 
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

            // update report area after each message is appended
            reportArea.repaint() ;	
        }
    } /* println */    
   
    /**
    * 
    * @param url - String : full URL 
    * @return String : unmodified page contents
    * @throws Exception - not handled
    *
    */
    private String getPage ( String url ) 
                            throws Exception
    // This method throws an exception back to its caller
    // so it should be used in a try {} catch {} structure
    // I just use catch to issue an error message (see the ActionEvent for the goButton)
    // if further diagnosis is needed then use try and catch within this method
    // and don't throw the Exception back to the caller.
    {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        println(1, "\nSending 'GET' request to URL : " + url);
	int responseCode = con.getResponseCode();
        println(1, "Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) 
        {
            // output individual lines
            // println(inputLine);
            response.append(inputLine);
        }
	
        in.close();			

        return response.toString() ;
    } /* String getPage ( String url ) */
	 
} // Class SwingGet
