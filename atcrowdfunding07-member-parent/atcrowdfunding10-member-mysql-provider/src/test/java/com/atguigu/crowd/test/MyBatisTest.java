package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.DetailProjectVO;
import com.atguigu.crowd.entity.vo.PortalProjectVO;
import com.atguigu.crowd.entity.vo.PortalTypeVO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    @Autowired
    private ProjectPOMapper projectPOMapper;

    Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Test
    public void testLoadDetailProjectVO(){
        Integer projectId = 1;
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        /*private Integer projectId;
    private String projectName;
    private String projectDesc;
    private Integer follower;
    private Integer status;
    private Integer money;
    private Integer supportMoeny;
    private Integer percentage;
    private String deployDate;
    private Integer lastDay;
    private Integer supporterCount;
    private String headerPicturePath;*/
        logger.debug(detailProjectVO.getProjectName());
        logger.debug(detailProjectVO.getProjectDesc());
        logger.debug(detailProjectVO.getStatus().toString());
        logger.debug(detailProjectVO.getMoney().toString());
        logger.debug(detailProjectVO.getSupporterCount().toString());
        logger.debug(detailProjectVO.getPercentage().toString());
        logger.debug(detailProjectVO.getDeployDate().toString());
        logger.debug(detailProjectVO.getHeaderPicturePath().toString());


        List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
        for (String s : detailPicturePathList) {
            logger.debug(s);
        }
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.info("connection = " + connection.toString());
    }

    @Test
    public void testInsertMember(){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encode = passwordEncoder.encode(rawPassword);

        MemberPO memberPO = new MemberPO(null, "jack1", encode, "杰克", "jack@qq.com", 1, 1, "杰克", "123456", 2);

        memberPOMapper.insert(memberPO);

    }

    @Test
    public void test(){
        List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
        for (PortalTypeVO portalVOList : portalTypeVOList) {
            String name = portalVOList.getName();
            String remark = portalVOList.getRemark();
            logger.info(name + " == " +remark);

            List<PortalProjectVO> portalProjectVOList = portalVOList.getPortalProjectVOList();


            for (PortalProjectVO portalProjectVO : portalProjectVOList) {
                if(portalProjectVO == null){
                    continue;
                }
                logger.info(portalProjectVO.toString());
            }
        }
    }
}
