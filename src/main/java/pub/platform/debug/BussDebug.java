package pub.platform.debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BussDebug {
     public static String absolutPath = "E:\\gsprj\\DevelopmentArea\\05Code\\04cims";
     public static boolean debug = false;

     public BussDebug() {
     }

     public static void recordObject(String objName, Object obj)
          throws Exception {
          File filepath = checkAndBuildPath();
          File file = new File(filepath, objName + ".out");
          if(!file.exists())
               writeObject(objName, obj);
     }

     public static void writeObject(String objName, Object oper)
          throws Exception {

          ObjectOutputStream out = null;
          try {
               File filepath = checkAndBuildPath();
               out = new ObjectOutputStream(new FileOutputStream(new File(filepath, objName + ".out")));
               out.writeObject(oper);
               out.close();
          } catch(Exception e) {
               e.printStackTrace();
               if(out != null)
                    out.close();
               throw e;
          }
     }

     public static Object readObject(String objName)
          throws Exception {
          ObjectInputStream in = null;
          try {

               File filepath = checkAndBuildPath();
               in = new ObjectInputStream(new FileInputStream(new File(filepath, objName + ".out")));
               Object obj = in.readObject();
               in.close();
               return obj;

          } catch(Exception e) {
               e.printStackTrace();
               if(in != null)
                    in.close();
               throw e;
          }
     }

     private static File checkAndBuildPath() {
          //先建目录

          File filepath = new File(absolutPath + File.separatorChar + "bussdebug");
          if(filepath.exists() && filepath.isDirectory()) {

          } else {
               filepath.mkdir();
          }

          return filepath;
     }

     /**
      * 引用信息处理
      * @return StackTraceElement
      */
     public static Throwable inferCaller() {
            // First, search back to a method in the Logger class.
//            System.out.println("1111>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//            (new Throwable()).printStackTrace();
//            System.out.println("1111<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

            return new Throwable();
    }

    public static String getInferMsg(String preMsg,String className) {
        StackTraceElement[] stack = new Throwable().getStackTrace();

        int ix = 0;
        while (ix < stack.length) {
            StackTraceElement frame = stack[ix];
            String cname = frame.getClassName();
            if (  cname.equals(className) ) {
                    ix++;
            } else {
                    break;
            }
        }
        
        if (ix == stack.length)
        	ix = stack.length -1;
        
        StackTraceElement st = stack[ix];
        
        String temp = preMsg+":"+st.getClassName()+"-->"+st.getMethodName()+":" + st.getLineNumber();
        System.out.println(temp);
        return temp;
    }

    public static void main(String[] args)
          throws Exception {
    	getInferMsg("test","web.debug.BussDebug");
    }

}
