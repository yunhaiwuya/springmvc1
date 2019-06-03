package ssm.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 常见的辅助类
 * 
 * @author ShenHuaJie
 * @since 2011-11-08
 */
public final class DataUtil {
	private static final Logger logger = LogManager.getLogger();
    private static SnowflakeIdWorker idWorker = new SnowflakeIdWorker(0,0);
    
	private DataUtil() {
	}

	/**
	 * 十进制字节数组转十六进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static final String byte2hex(byte[] b) { // 一个字节数，转成16进制字符串
		StringBuilder hs = new StringBuilder(b.length * 2);
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			// 整数转成十六进制表示
			stmp = Integer.toHexString(b[n] & 0XFF);
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString(); // 转成大写
	}

	/**
	 * 十六进制字符串转十进制字节数组
	 * 
	 * @param b
	 * @return
	 */
	public static final byte[] hex2byte(String hs) {
		byte[] b = hs.getBytes();
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			// 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个十进制字节
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}
		return b2;
	}

	/**
	 * 这个方法可以通过与某个类的class文件的相对路径来获取文件或目录的绝对路径。 通常在程序中很难定位某个相对路径，特别是在B/S应用中。
	 * 通过这个方法，我们可以根据我们程序自身的类文件的位置来定位某个相对路径。
	 * 比如：某个txt文件相对于程序的Test类文件的路径是../../resource/test.txt，
	 * 那么使用本方法Path.getFullPathRelateClass("../../resource/test.txt",Test.class)
	 * 得到的结果是txt文件的在系统中的绝对路径。
	 * 
	 * @param relatedPath 相对路径
	 * @param cls 用来定位的类
	 * @return 相对路径所对应的绝对路径
	 * @throws IOException 因为本方法将查询文件系统，所以可能抛出IO异常
	 */
	public static final String getFullPathRelateClass(String relatedPath, Class<?> cls) {
		String path = null;
		if (relatedPath == null) {
			throw new NullPointerException();
		}
		String clsPath = getPathFromClass(cls);
		File clsFile = new File(clsPath);
		String tempPath = clsFile.getParent() + File.separator + relatedPath;
		File file = new File(tempPath);
		try {
			path = file.getCanonicalPath();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return path;
	}

	/**
	 * 获取class文件所在绝对路径
	 * 
	 * @param cls
	 * @return
	 * @throws IOException
	 */
	public static final String getPathFromClass(Class<?> cls) {
		String path = null;
		if (cls == null) {
			throw new NullPointerException();
		}
		URL url = getClassLocationURL(cls);
		if (url != null) {
			path = url.getPath();
			if ("jar".equalsIgnoreCase(url.getProtocol())) {
				try {
					path = new URL(path).getPath();
				} catch (MalformedURLException e) {
				}
				int location = path.indexOf("!/");
				if (location != -1) {
					path = path.substring(0, location);
				}
			}
			File file = new File(path);
			try {
				path = file.getCanonicalPath();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
		return path;
	}

	/**
	 * 判断对象是否Empty(null或元素为0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj 待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static final boolean isEmpty(Object pObj) {
		if (pObj == null)
			return true;
		if (pObj == "")
			return true;
		if (pObj instanceof String) {
			if (((String) pObj).trim().length() == 0) {
				return true;
			}
		} else if (pObj instanceof Collection<?>) {
			if (((Collection<?>) pObj).size() == 0) {
				return true;
			}
		} else if (pObj instanceof Map<?, ?>) {
			if (((Map<?, ?>) pObj).size() == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断对象是否为NotEmpty(!null或元素>0)<br>
	 * 实用于对如下对象做判断:String Collection及其子类 Map及其子类
	 * 
	 * @param pObj 待检查对象
	 * @return boolean 返回的布尔值
	 */
	public static final boolean isNotEmpty(Object pObj) {
		if (pObj == null)
			return false;
		if (pObj == "")
			return false;
		if (pObj instanceof String) {
			if (((String) pObj).trim().length() == 0) {
				return false;
			}
		} else if (pObj instanceof Collection<?>) {
			if (((Collection<?>) pObj).size() == 0) {
				return false;
			}
		} else if (pObj instanceof Map<?, ?>) {
			if (((Map<?, ?>) pObj).size() == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * JS输出含有\n的特殊处理
	 * 
	 * @param pStr
	 * @return
	 */
	public static final String replace4JsOutput(String pStr) {
		pStr = pStr.replace("\r\n", "<br/>&nbsp;&nbsp;");
		pStr = pStr.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
		pStr = pStr.replace(" ", "&nbsp;");
		return pStr;
	}

	/**
	 * 分别去空格
	 * 
	 * @param paramArray
	 * @return
	 */
	public static final String[] trim(String[] paramArray) {
		if (ArrayUtils.isEmpty(paramArray)) {
			return paramArray;
		}
		String[] resultArray = new String[paramArray.length];
		for (int i = 0; i < paramArray.length; i++) {
			String param = paramArray[i];
			resultArray[i] = StringUtils.trim(param);
		}
		return resultArray;
	}

	/**
	 * 获取类的class文件位置的URL
	 * 
	 * @param cls
	 * @return
	 */
	private static URL getClassLocationURL(final Class<?> cls) {
		if (cls == null)
			throw new IllegalArgumentException("null input: cls");
		URL result = null;
		final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
		final ProtectionDomain pd = cls.getProtectionDomain();
		if (pd != null) {
			final CodeSource cs = pd.getCodeSource();
			if (cs != null)
				result = cs.getLocation();
			if (result != null) {
				if ("file".equals(result.getProtocol())) {
					try {
						if (result.toExternalForm().endsWith(".jar") || result.toExternalForm().endsWith(".zip"))
							result = new URL("jar:".concat(result.toExternalForm()).concat("!/").concat(clsAsResource));
						else if (new File(result.getFile()).isDirectory())
							result = new URL(result, clsAsResource);
					} catch (MalformedURLException ignore) {
					}
				}
			}
		}
		if (result == null) {
			final ClassLoader clsLoader = cls.getClassLoader();
			result = clsLoader != null ? clsLoader.getResource(clsAsResource)
					: ClassLoader.getSystemResource(clsAsResource);
		}
		return result;
	}

	/** 初始化设置默认值 */
	public static final <K> K ifNull(K k, K defaultValue) {
		if (k == null) {
			return defaultValue;
		}
		return k;
	}
	
	  /**
     * 把元转成分
     * @param tradeAmount
     * @return
     */
    public static String changeToDivide(String tradeAmount){
        if(tradeAmount == null || "".equals(tradeAmount)){
            return "";
        }
        BigDecimal amt = new BigDecimal(tradeAmount);
        BigDecimal divide = new BigDecimal("100");
        return amt.multiply(divide).setScale(0).toString();
    }
    
    /**
     * 把元转成分
     * liming
     */
    public static BigInteger changeToCent(String tradeAmount){
        if(tradeAmount == null || "".equals(tradeAmount)){
            return BigInteger.valueOf(0);
        }
        BigDecimal amt = new BigDecimal(tradeAmount);
        BigDecimal divide = new BigDecimal(100);
        return amt.multiply(divide).toBigInteger();
    }
    /**
     * 判断是否全部为中文，是则返回false，只要有一个不是中文就返回true
     * @param str
     * @return
     */
    public static boolean isChineseChar(String str){
        boolean temp = false;
        for(int i=0; i<str.length(); i++){
            Pattern p=Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m=p.matcher(String.valueOf(str.charAt(i)));
            if(m.find()){
                temp =  false;
            }else{
                temp = true;
                return temp;
            }
        }
        return temp;
    }
	  
    /**
     * 
     * Description: 创建6位随机数
     *
     * @return 
     * @see
     */
    public static String createSmsCode()
    {
        String str="0123456789";
        StringBuilder sb=new StringBuilder(4);
        for(int i=0;i<6;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
    
    /** 
     * 获得一个UUID 
     * @return String UUID 
     */ 
    public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return  s.replace("-", ""); 
    } 
    
    /**
     * 去除字符串前面所有的0，比如 000000154-->154
     * @param str
     * @return
     */
    public static String subBeforZero(String str)
    {
        String res = "";
        if(StringUtils.isNotEmpty(str))
        {
            for(int i=0; i<str.length();i++)
            {
                char c = str.charAt(i);
                if(c !='0')
                {
                    res = str.substring(i, str.length());
                    break;
                }
            }
        }
        return res;
    }
    
    /**
     * 
     * Description: 在数字前面累加0
     * @param num 需要累加几位0
     * @param id 需要累加的数据
     * @return 
     * @see
     */
    public static String fullBeforZero(int num,int id)
    {
        String str = String.format("%0"+num+"d", id);
        return str;
    }
    /**
     * 
     * Description: 生成展示订单编号
     * 规则  日期8位+省市区6位+四位随机数+手机号尾号  如： 20170526110107+
     * @param areaNo 区域编号
     * @param id 订单唯一键值id
     * @return 
     * @see
     */
    
    public static String createOrderNo(String areaNo,String tel)
    {
        StringBuffer result= new StringBuffer();
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String head = format.format(date);
        result.append(head);
        result.append(areaNo);
        String end= tel.substring(tel.length()-4,tel.length());
        result.append(end);
        result.append(createSmsCode());
        return result.toString();
    }
    
    /**
     * 获取订单真实编号或者系统流水号
     * Description: <br>
     * Implement: <br>
     *
     * @return 
     * @see
     */
    public static String getOderNumber()
    {
        return String.valueOf(idWorker.nextId());
    }
    
    /**
     * 
     * Description: 取出距离相同的数据
     *
     * @param params
     * @return 
     * @see
     */
    public static List<Map<String,Object>> getCommonData(List<Map<String,Object>> params)
    {
        List<Map<String,Object>>  result = new ArrayList<Map<String,Object>>();
        if(params != null && !"".equals(params))
        {
            int i=1;
            Double index = 0d;
            for(Map<String,Object> pp : params)
            {

                if(pp.get("distant") != null && !"".equals(pp.get("distant")))
                {
                    Double th = Double.valueOf(pp.get("distant").toString());;
                    if(i==1)
                    {
                        index = th;
                        result.add(pp);
                    }
                    else if(index.equals(th))
                    {
                        result.add(pp);
                    }
                    i++;
                } 
            }
        }
        return result;
    }
    
    /**
     * 
     * Description: 根据后缀名获取编码格式
     *
     * @param endPix
     * @return 
     * @see
     */
    public static String getMineType(String endPix)
    {
        String reString="";
        if("3gp".equals(endPix))
        {
            reString="video/3gpp";
        }
        else if("mp4".equals(endPix))
        {
            reString="video/mpeg4";
        }
        else if("rmvb".equals(endPix))
        {
            reString="application/vnd.rn-realmedia-vbr";
        }
        else if("mov".equals(endPix))
        {
            reString="video/quicktime";
        }
        else if("avi".equals(endPix))
        {
            reString="video/avi";
        }
        return reString;
    }
    
    public static void main(String[] args)
    {
        
//        for(int i=0;i<10;i++)
//        {
//            
//            
//            System.out.println(getOderNumber());
//            
//            Thread thread= new Thread(){
//                @Override  
//                public void run() { 
//                     for(int i=0;i<10;i++)
//                        System.out.println("----"+getOderNumber());
//                }
//            };
//            thread.start();
//        }
        
       /* List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
        Map<String,Object> th = new HashMap<String,Object>();
        th.put("distant", "12121.223");
        th.put("pp", "1");
        params.add(th);
        Map<String,Object> th1 = new HashMap<String,Object>();
        th1.put("distant", "121213.223");
        th1.put("pp", "2");
        params.add(th1);
        Map<String,Object> th2 = new HashMap<String,Object>();
        th2.put("distant", "128214.223");
        th2.put("pp", "3");
        params.add(th2);
        Map<String,Object> th3 = new HashMap<String,Object>();
        th3.put("distant", "121233.223");
        th3.put("pp", "4");
        params.add(th3);
        Map<String,Object> th4 = new HashMap<String,Object>();
        th4.put("distant", "121223.223");
        th4.put("pp", "4");
        params.add(th4);
        Map<String,Object> th5 = new HashMap<String,Object>();
        th5.put("distant", "121231.223");
        th5.put("pp", "5");
        params.add(th5);
        
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            String json=  mapper.writeValueAsString(getCommonData(params));
            System.out.println("json-----"+json);
        }
        catch (JsonProcessingException e)
        {
            logger.error(e.getMessage());
        }*/
        
    }
}