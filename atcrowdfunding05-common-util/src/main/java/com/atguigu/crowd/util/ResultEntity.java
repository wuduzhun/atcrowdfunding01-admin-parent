package com.atguigu.crowd.util;

/**
 * 统一整个项目中Ajax请求返回的结果（未来也可以用于分布式架构各个模块间调用返回统一类型）
 *
 * @author 威少准
 * @version
 * @param <T>
 */
public class ResultEntity<T>
{
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String NO_MESSAGE = "NO_MESSAGE";
    public static final String NO_DATA = "NO_DATA";

    // 用来封装当前请求处理的结果是成功还是失败
    private String result;

    // 请求处理失败时返回的错误信息
    private String message;

    // 要返回的数据
    private T data;



    /**
     * 请求处理成功且不需要返回数据时使用的工具方法
     * @param <Type>
     * @return
     */
    public static ResultEntity<String> successWithoutData()
    {
        return new ResultEntity<String >(SUCCESS,NO_MESSAGE , NO_DATA);
    }

    /**
     * 请求处理成功且需要返回数据时使用的工具方法
     * @param data 要返回的数据
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> successWithData(Type data)
    {
        return new ResultEntity<Type>(SUCCESS, NO_MESSAGE, data);
    }

    /**
     * 请求处理失败后使用的工具方法
     * @param message 失败错误信息
     * @param <Type>
     * @return
     */
    public static <Type> ResultEntity<Type> failed(String message)
    {
        return new ResultEntity<Type>(FAILED,message, null);
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T data) {
        this.result = result;
        this.message = message;
        this.data = data;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
