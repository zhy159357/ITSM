import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dayong_sun
 */

final public class JudgeDB {

	/**
     * Logger for this class
     */
	private static final Logger _log = LoggerFactory.getLogger(JudgeDB.class);
    
    private JudgeDB(){

    }
    
    /**
     * @throws Exception 
     */
    public static void _init(String environment) {
    	DB_TYPE_FLAG = environment;
		
		if(("oracle").equals(DB_TYPE_FLAG)){
			IEAI_DB_TYPE = 1;
		}else if(("mysql").equals(DB_TYPE_FLAG)){
		    IEAI_DB_TYPE = 2;
		}else{
			_log.info("get db type error,may the configuration not config!");
			try {
				throw new Exception("get DB type error!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }
	
	static public String DB_TYPE_FLAG = null;
	public static Integer IEAI_DB_TYPE = 1;
	
	public static Integer ORACLE=1;
	public static Integer MYSQL=2;
}
