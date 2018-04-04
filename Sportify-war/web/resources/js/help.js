/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




function getLastDayOfYearAndMonth(year, month)
{
    return(new Date((new Date(year, month + 1, 1)) - 1)).getDate();
}

function disableFromDays(date) {
   
    //var date = $(this).datepicker('getDate'),
    var date = date.getDate();
    console.log("date is " + date);
    if (date === 1) {
        return [true, 'customerFromDate'];
    }
    return [false, 'customerFromDate'];
    //return [(day !== 0 && day !== 6), 'customerFromDate'];
}

function disableToDays(date) {

    var dates = date.getDate();
    var todayDates = new Date();
   // console.log(date);
    
    
    
    if (dates === getLastDayOfYearAndMonth(date.getFullYear(), date.getMonth()) || (todayDates.getDate() === date.getDate() && todayDates.getMonth() === date.getMonth() && todayDates.getFullYear() === date.getFullYear()))
    {
        return [true, 'customerToDate'];
    }
    
    if(todayDates.getDate() < getLastDayOfYearAndMonth(date.getFullYear(), date.getMonth()) && todayDates.getMonth() === date.getMonth() && todayDates.getFullYear() === date.getFullYear())
    {
        
        console.log("BIGAAJD");
        return [false, 'customerToDate'];
    }
    
 
    return [false, 'customerToDate'];
}

function chartExtender() {
    //this = chart widget instance
    //this.cfg = options
    // this.cfg.axes.xaxis.tickOptions.showGridline = false;
    // this.cfg.axes.yaxis.tickOptions.showGridline = false;
    this.cfg.grid = {gridLineColor: 'lightGray'};
    /*  this.cfg.axes.yaxis.tickOptions = {
     textColor : '#ffffff'
     };
     this.cfg.axes.xaxis.tickOptions = {
     textColor : '#ffffff'
     };
     //        //  this.cfg.series = [{seriesColors: ["red" ,"red", "red","red","red","red","red","red","red","red","red"]}];*/
    
   
}


function pieExtender(){
   this.cfg.grid = {background: '#DCDCDC' , borderColor: '#DCDCDC'};
}

function barExtender(){
    
    this.cfg.seriesDefaults.rendererOptions = {   barWidth: 60   };
    
    this.cfg.axes.yaxis.tickOptions  = { textColor: '#ffffff' };
    this.cfg.axes.yaxis.labelOptions = {fontSize: '13pt', textColor: '#ffffff' , fontFamily: 'Trajan Pro'}; 
     this.cfg.axes.xaxis.tickOptions  = { textColor: '#ffffff' ,  showGridline: false };
      this.cfg.axes.xaxis.labelOptions = {fontSize: '13pt'  , textColor: '#ffffff' , fontFamily: 'Trajan Pro' }; 
 //FFA500    
}
  