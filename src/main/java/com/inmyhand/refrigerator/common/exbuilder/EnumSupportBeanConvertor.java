package com.inmyhand.refrigerator.common.exbuilder;

import com.cleopatra.protocol.data.DefaultBeanConvertor;
import com.cleopatra.protocol.data.ParameterRow;
import com.inmyhand.refrigerator.error.exception.EnumParsingError;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public class EnumSupportBeanConvertor<B> extends DefaultBeanConvertor<B> {

    public EnumSupportBeanConvertor(Class<B> beanClass) {
        super(beanClass);
    }

    @Override
    public B convert(ParameterRow row) {
        // 부모 클래스의 convert 메서드로 변환
        B beanInstance = super.convert(row);

        // enum 타입에 대한 추가 처리
        try {
            // 모든 setter 메서드 가져오기
            Method[] methods = beanInstance.getClass().getMethods();

            for (String columnName : row.getColumnNames()) {
                String data = row.getValue(columnName);
                if (data == null || data.isEmpty()) continue;

                String key = columnName.toLowerCase().replaceAll("[_-]", "");

                // setter 메서드 찾기
                for (Method method : methods) {
                    String name = method.getName().toLowerCase();
                    if (name.startsWith("set") && name.substring(3).equals(key)) {
                        Class<?>[] paramTypes = method.getParameterTypes();
                        if (paramTypes.length == 1 && paramTypes[0].isEnum()) {
                            try {
                                @SuppressWarnings({"unchecked", "rawtypes"})
                                Object enumValue = Enum.valueOf((Class<Enum>) paramTypes[0], data);
                                method.invoke(beanInstance, enumValue);
                            } catch (Exception e) {
                                log.error("Failed to convert '" + data + "' to enum type " + paramTypes[0].getName());
                            }
                        }
                    }
                }
            }

            return beanInstance;
        } catch (Exception e) {
            throw new EnumParsingError("Failed to convert " + beanInstance.getClass().getName());
        }
    }
}
