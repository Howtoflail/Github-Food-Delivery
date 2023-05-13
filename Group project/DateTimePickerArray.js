import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import { Navigation } from "swiper";
import "swiper/css/navigation";
import "./datetimepicker.css";
import axios from "axios";
import { useEffect, useState } from "react";
import moment from "moment/moment";
import TimeList from "./TimeList";

const DateTimePickerArray = () => {
  //This is the full array containing two weeks of time slots
  //With updated states of taken or not
  const [timesArray, setTimesArray] = useState([]);

  useEffect(() => {
    let nextWeek = [];
    let currWeek = [];     

    let dayCurrWk = new Date();
    let dayNextWk = new Date();
    dayNextWk.setDate(dayCurrWk.getDate() +7);

    //gets today's date and sets the date to the 1st day of the week
    dayCurrWk.setDate((dayCurrWk.getDate() - dayCurrWk.getDay() +1));
    dayNextWk.setDate((dayNextWk.getDate() - dayNextWk.getDay() +1));

    for (let i = 0; i < 5; i++) {
        currWeek.push(
            new Date(dayCurrWk)
        ); 

        nextWeek.push(
            new Date(dayNextWk)
        ); 

        dayCurrWk.setDate(dayCurrWk.getDate() +1);
        dayNextWk.setDate(dayNextWk.getDate() +1);
    }
    let dates = [];
    currWeek.forEach((date) => {
      dates.push(date);
    });
    nextWeek.forEach((date) => {
      dates.push(date);
    });
    //console.log(dates);

    let timeSlots = [];
    dates.forEach((date) => {
      let day = date.getDate();
      let month = date.getMonth() + 1;
      let year = date.getFullYear();
      let time = new Date(`${month} ${day}, ${year} 09:00:00`);
      //console.log(time);
      for(let i = 0; i < 16; i++){
        //make the half hour timeslots here
        let timeSlot = {
          timeUtc: time,
          endTimeUtc: moment(time).add(30, "m").toDate(),
          taken: false,
        };
        timeSlots.push(timeSlot);

        time = moment(time).add(30, "m").toDate();
      }
    });
    //console.log(timeSlots);
    const postObject = {
      firstName: "bruh",
      lastName: "bruh",
      email: "AdeleV@x2f56.onmicrosoft.com"
    };

    let busyTimeSlots = [];
    axios.post("https://localhost:7058/api/Recruiter/Timeslot", postObject).then((res) => {
      res.data[0].scheduleItems.forEach((element) => {
        busyTimeSlots.push(element);
      });
      //work with busyTimeSlots array here because outside of then() it doesnt work
      console.log(busyTimeSlots);

      busyTimeSlots.forEach((busyTimeSlot) => {
        let startDate = new Date(Date.parse(busyTimeSlot.start.dateTime));
        let endDate = new Date(Date.parse(busyTimeSlot.end.dateTime));

        timeSlots.forEach((timeSlot) => {
          if((startDate <= timeSlot.timeUtc && endDate > timeSlot.timeUtc) ||
          (startDate >= timeSlot.timeUtc && startDate < timeSlot.endTimeUtc && endDate > startDate)){
            timeSlot.taken = true;
          }
        });
      });

      timeSlots.forEach((timeSlot) => {
        timeSlot.timeUtc = timeSlot.timeUtc.toISOString();
        timeSlot.endTimeUtc = timeSlot.endTimeUtc.toISOString();
      });
      setTimesArray(timeSlots);
      console.log(timeSlots);
    });
  }, []);
  console.log(timesArray);

  return (
    <div className="container">
    </div>
  );
};

export default DateTimePickerArray;
