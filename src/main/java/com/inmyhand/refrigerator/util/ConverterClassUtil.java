package com.inmyhand.refrigerator.util;
import java.util.List;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.ParameterGroup;
import com.cleopatra.protocol.data.RowState;
import com.inmyhand.refrigerator.common.exbuilder.EnumSupportBeanConvertor;
import com.inmyhand.refrigerator.error.exception.ParamGroupIsNullException;
import lombok.extern.slf4j.Slf4j;

/**
 * DataRequest에서 DTO 객체로 변환하는 유틸리티 클래스
 */
@Slf4j
public class ConverterClassUtil {

    /**
     * DataRequest에서 단일  객체 가져오기
     * @param <T> 반환할  타입
     * @param dataRequest 데이터 요청 객체
     * @param paramGroupId 파라미터 그룹 ID
     * @param className 변환할 클래스
     * @return 단일 DTO 객체
     */
    public static <T> T getSingleClass(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        ParameterGroup paramGroup = dataRequest.getParameterGroup(paramGroupId);
        if (paramGroup == null) {
            log.error("[getSingleClass] paramGroupId 값이 없습니다.");
            throw new ParamGroupIsNullException("paramGroupId 값이 없습니다.");
        }

        // DefaultBeanConvertor를 확장한 Enum을 지원하는 컨버터 사용
        EnumSupportBeanConvertor<T> convertor = new EnumSupportBeanConvertor<>(className);
        convertor.setDateFormat("yyyy-MM-dd"); // 날짜 포멧

        return className.cast(paramGroup.getBeanData(convertor));
    }

    /**
     * DataRequest에서 Class 객체 리스트 가져오기
     * @param <T> 반환할 Class 타입
     * @param dataRequest 데이터 요청 객체
     * @param paramGroupId 파라미터 그룹 ID
     * @param className 변환할 Class 클래스
     * @return DTO 객체 리스트
     */
    public static <T> List<T> getClassList(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        ParameterGroup paramGroup = dataRequest.getParameterGroup(paramGroupId);
        if (paramGroup == null) {
            log.error("[getClassList] paramGroupId 값이 없습니다.");
            throw new ParamGroupIsNullException("paramGroupId 값이 없습니다.");
        }

        // DefaultBeanConvertor를 확장한 Enum을 지원하는 컨버터 사용
        EnumSupportBeanConvertor<T> convertor = new EnumSupportBeanConvertor<>(className);
        convertor.setDateFormat("yyyy-MM-dd"); // 날짜 포멧

        return paramGroup.getAllBeanList(convertor);
    }


    public static <T> List<T> getClassList(DataRequest dataRequest, String paramGroupId, Class<T> className, String date) {
        ParameterGroup paramGroup = dataRequest.getParameterGroup(paramGroupId);
        if (paramGroup == null) {
            log.error("[getClassList] paramGroupId 값이 없습니다.");
            throw new ParamGroupIsNullException("paramGroupId 값이 없습니다.");
        }

        // DefaultBeanConvertor를 확장한 Enum을 지원하는 컨버터 사용
        EnumSupportBeanConvertor<T> convertor = new EnumSupportBeanConvertor<>(className);
        convertor.setDateFormat(date); // 날짜 포멧

        return paramGroup.getAllBeanList(convertor);
    }


    /**
     * 특정 상태의 DTO 객체 리스트 가져오기 (추가/수정/삭제 등)
     * @param <T> 반환할 Class 타입
     * @param dataRequest 데이터 요청 객체
     * @param paramGroupId 파라미터 그룹 ID
     * @param className 변환할 Class 클래스
     * @param rowState 행 상태 (INSERTED, UPDATED, DELETED 등)
     * @return 특정 상태의 Class 객체 리스트
     */
    private static <T> List<T> getStatedClassList(DataRequest dataRequest, String paramGroupId,
                                                  Class<T> className, RowState rowState) {
        ParameterGroup paramGroup = dataRequest.getParameterGroup(paramGroupId);
        if (paramGroup == null) {
            log.error("[getStatedClassList] paramGroupId 값이 없습니다.");
            throw new ParamGroupIsNullException("paramGroupId 값이 없습니다.");
        }

        // DefaultBeanConvertor를 확장한 Enum을 지원하는 컨버터 사용
        EnumSupportBeanConvertor<T> convertor = new EnumSupportBeanConvertor<>(className);
        convertor.setDateFormat("yyyy-MM-dd"); // 날짜 포멧

        // 커스텀 컨버터를 사용하여 지정된 상태의 Bean 리스트 변환
        return paramGroup.getStatedBeanList(rowState, convertor);
    }

//
//    /**
//     * DataRequest에서 자동으로 단일 객체 또는 리스트 반환 (ParameterGroup 타입에 따라)
//     * @param <T> 반환할 Class 타입
//     * @param dataRequest 데이터 요청 객체
//     * @param paramGroupId 파라미터 그룹 ID
//     * @param className 변환할 Class 클래스
//     * @return 단일 Class 객체 또는 Class 객체 리스트
//     */

/*    @SuppressWarnings("unchecked")
    public static <T> Object getClass(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        ParameterGroup paramGroup = dataRequest.getParameterGroup(paramGroupId);
        if (paramGroup == null) {
            return null;
        }
        return paramGroup.getBeanData(className);
    }*/



    /**
     * 추가된(INSERTED) 데이터만 Class 리스트로 반환
     */
    public static <T> List<T> getInsertedClassList(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        return getStatedClassList(dataRequest, paramGroupId, className, RowState.INSERTED);
    }

    /**
     * 수정된(UPDATED) 데이터만 DTO 리스트로 반환
     */
    public static <T> List<T> getUpdatedClassList(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        return getStatedClassList(dataRequest, paramGroupId, className, RowState.UPDATED);
    }

    /**
     * 삭제된(DELETED) 데이터만 DTO 리스트로 반환
     */
    public static <T> List<T> getDeletedClassList(DataRequest dataRequest, String paramGroupId, Class<T> className) {
        return getStatedClassList(dataRequest, paramGroupId, className, RowState.DELETED);
    }
}
