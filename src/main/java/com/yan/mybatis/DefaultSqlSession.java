package com.yan.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hairui
 * @date 2021/11/11
 * @des
 */
public class DefaultSqlSession implements SqlSession{

    private Connection connection;
    private Map<String, XNode> mapperElement;

    public DefaultSqlSession(Connection connection, Map<String, XNode> mapperElement) {
        this.connection = connection;
        this.mapperElement = mapperElement;
    }

    @Override
    public <T> T selectOne(String statement) {

        try{
            //解析sql
            XNode xNode = mapperElement.get(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            //执行sql
            ResultSet resultSet = preparedStatement.executeQuery();
            //结果封装
            List<T> objects = resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        }catch (Exception e){
            return null;
        }
    }

    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> forName) {
        List<T> resultList = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()){
                T obj = (T) forName.newInstance();
                for (int count=0;count<=columnCount;count++) {
                    Object value = resultSet.getObject(count);
                    String columnName = metaData.getColumnName(count);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = forName.getMethod(setMethod, Date.class);
                    } else {
                        method = forName.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj,value);
                }
                resultList.add(obj);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
            return resultList;
    }

    private void buildParameter(PreparedStatement preparedStatement, Object parameter, Map<Integer, String> parameterMap) throws SQLException, IllegalAccessException {

        int size = parameterMap.size();
        // 单个参数
        if (parameter instanceof Long) {
            for (int i = 1; i <= size; i++) {
                preparedStatement.setLong(i, Long.parseLong(parameter.toString()));
            }
            return;
        }

        if (parameter instanceof Integer) {
            for (int i = 1; i <= size; i++) {
                preparedStatement.setInt(i, Integer.parseInt(parameter.toString()));
            }
            return;
        }

        if (parameter instanceof String) {
            for (int i = 1; i <= size; i++) {
                preparedStatement.setString(i, parameter.toString());
            }
            return;
        }

        Map<String, Object> fieldMap = new HashMap<>();
        // 对象参数
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();
            field.setAccessible(true);
            Object obj = field.get(parameter);
            field.setAccessible(false);
            fieldMap.put(name, obj);
        }

        for (int i = 1; i <= size; i++) {
            String parameterDefine = parameterMap.get(i);
            Object obj = fieldMap.get(parameterDefine);

            if (obj instanceof Short) {
                preparedStatement.setShort(i, Short.parseShort(obj.toString()));
                continue;
            }

            if (obj instanceof Integer) {
                preparedStatement.setInt(i, Integer.parseInt(obj.toString()));
                continue;
            }

            if (obj instanceof Long) {
                preparedStatement.setLong(i, Long.parseLong(obj.toString()));
                continue;
            }

            if (obj instanceof String) {
                preparedStatement.setString(i, obj.toString());
                continue;
            }

            if (obj instanceof Date) {
                preparedStatement.setDate(i, (java.sql.Date) obj);
            }

        }

    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        //解析sql
        XNode xNode = mapperElement.get(statement);
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(xNode.getSql());
            //封装参数
            buildParameter(preparedStatement,parameter, xNode.getParameter());
            //执行sql
            ResultSet resultSet = preparedStatement.executeQuery();
            //结果封装
            List<T> objects = resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        } catch (SQLException |IllegalAccessException |ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement) {
        XNode xNode = mapperElement.get(statement);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        XNode xNode = mapperElement.get(statement);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            //封装参数
            buildParameter(preparedStatement,parameter, xNode.getParameter());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet2Obj(resultSet, Class.forName(xNode.getResultType()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() {
        if (null == connection) return;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
