package com.obg.bookmeeting;

import com.obg.bookmeeting.common.LuisServer;
import com.obg.bookmeeting.common.Util;
import com.obg.bookmeeting.service.BookMeetingService;
import com.obg.bookmeeting.service.BotService;
import com.obg.bookmeeting.vo.LuisResult;
import com.obg.bookmeeting.vo.MeetingRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookmeetingApplicationTests {

	@Autowired
    BookMeetingService bookMeetingService;

    @Autowired
    BotService botService;

	@Test
	public void contextLoads() {
        LuisResult luisResult = LuisServer.getLuisResponse("我先顶一个会议室，星期二上午9点到7点");
        MeetingRoom meetingRoom = new MeetingRoom();
        botService.updataMeetingRoomPatameters(luisResult, meetingRoom);
        System.out.println(meetingRoom.toString());
    }

	@Test
	public void testGetLuisResult(){

		String text = "我想订哥会议室，明天上午9点到10点，20个人,1个小时";
		bookMeetingService.bookMeeting("","", text);
	}

	/**
	 * 检查获取到的会议室时间，并转化为标准有效时间
	 */
	@Test
	public void testMeetingTime(){
        MeetingRoom meetingRoom = new MeetingRoom();
        Util.setMeetingTimes("09:00:00", "01:00:00", meetingRoom);
        System.out.println(meetingRoom.getStartTime().getAttrName());
        System.out.println(meetingRoom.getEndTime().getAttrName());

        Util.setMeetingTimes("06:30:00", "08:00:00", meetingRoom);
        System.out.println(meetingRoom.getStartTime().getAttrName());
        System.out.println(meetingRoom.getEndTime().getAttrName());

        Util.setMeetingTimes("06:30:00", "09:30:00", meetingRoom);
        System.out.println(meetingRoom.getStartTime().getAttrName());
        System.out.println(meetingRoom.getEndTime().getAttrName());

        Util.setMeetingTimes("01:00:00", "04:00:00", meetingRoom);
        System.out.println(meetingRoom.getStartTime().getAttrName());
        System.out.println(meetingRoom.getEndTime().getAttrName());

        Util.setMeetingTimes("09:00:00", "11:30:00", meetingRoom);
        System.out.println(meetingRoom.getStartTime().getAttrName());
        System.out.println(meetingRoom.getEndTime().getAttrName());
    }
}
