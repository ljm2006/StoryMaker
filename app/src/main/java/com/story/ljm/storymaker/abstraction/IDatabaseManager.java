package com.story.ljm.storymaker.abstraction;

/**
 * Created by ljm on 2017-05-15.
 */

public interface IDatabaseManager {
    void openDB();//db생성 및 열기

    //테이블 생성 메소드
    void createStoryListTable();//테이블 제목, 글 내용이 들어있는 table 생성
    //소재 관련 내용이 들이있는 table 생성
    void createCharacterResourceTable(int storyID);
    void createPlaceResourceTable(int storyID);
    void createConceptResourceTable(int storyID);

    //데이터 추가 메소드
    void insertStoryListTableData(int story_id, String title, String content);
    void insertCharacterResourceData(int storyID, int res_id, String resName, int priority, String comment);
    void insertPlaceResourceData(int storyID, int res_id, String resName, int priority, String comment);
    void insertConceptResourceData(int storyID, int res_id, String resName, int priority, String comment);

    //데이터 삭제 메소드
    void deleteStoryListTableData(int story_id);
    //void deleteStoryResourceTableData(int res_id);
    void deleteCharacterResourceData(int storyID, int resID);
    void deletePlaceResourceData(int storyID, int resID);
    void deleteConceptResourceData(int storyID, int resID);

    //데이터 수정 메소드
    void updateStoryListTableData(int story_id, String title, String content);
    void updateCharacterResourceData(int storyID, int resID, String resName, int priority, String comment);
    void updatePlaceResourceData(int storyID, int resID, String resName, int priority, String comment);
    void updateConceptResourceData(int storyID, int resID, String resName, int priority, String comment);

    //쿼리 메소드
    String getQueryResult();//List query 결과 리턴, json 방식으로 리턴
    String getResourceQuery(String tableName, int story_id);//Resource query 결과 리턴. json 방식.

    //구조가 같은 테이블에 복사
    void copyResourceData(int pastedStoryID, int copiedStoryID);

}
