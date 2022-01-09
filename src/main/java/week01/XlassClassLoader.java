package week01;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

/**
 * <p>
 * XlassClassLoader
 * </p>
 *
 * @author Xu Li, 2022年01月09日
 */
public class XlassClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        // 相关参数
        final String className = "week01.Hello";
        final String methodName = "hello";
        // 创建类加载器
        XlassClassLoader classLoader = new XlassClassLoader();
        // 加载相应的类,会在底层调用findClass方法
        Class<?> clazz = classLoader.loadClass(className);
        // 创建对象
        Object instance = clazz.getDeclaredConstructor().newInstance();
        // 调用实例方法
        Method method = clazz.getMethod(methodName);
        method.invoke(instance);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = new File("/Users/macxu/Documents/coding/javacourse/src/resources/week01/Hello.xlass");
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            // 读取数据
            byte[] byteArray = new byte[inputStream.available()];
            inputStream.read(byteArray);
            // 转换
            byte[] classBytes = decode(byteArray);
            // 通知底层定义这个类
            return defineClass(name, classBytes, 0, classBytes.length);
        } catch (IOException e) {
            throw new ClassNotFoundException(name, e);
        } finally {
            close(inputStream);
        }
    }

    // 解码
    private static byte[] decode(byte[] byteArray) {
        byte[] targetArray = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            targetArray[i] = (byte) ~byteArray[i];
        }
        return targetArray;
    }

    // 关闭
    private static void close(Closeable res) {
        if (null != res) {
            try {
                res.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
