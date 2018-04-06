/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.helperClass;

/**
 *
 * @author David
 */
public class PerformanceBoard {
    
    private String titleInformation;
    private String totalInformation;
    private String totalValue;
    private String currentMonthInformation;
    private String figureInformation;
    private String figureStatus;

    public PerformanceBoard() {
    }

    public PerformanceBoard(String titleInformation, String totalInformation, String totalValue, String currentMonthInformation, String figureInformation, String figureStatus) {
        this.titleInformation = titleInformation;
        this.totalInformation = totalInformation;
        this.totalValue = totalValue;
        this.currentMonthInformation = currentMonthInformation;
        this.figureInformation = figureInformation;
        this.figureStatus = figureStatus;
    }

    
    
    
    /**
     * @return the titleInformation
     */
    public String getTitleInformation() {
        return titleInformation;
    }

    /**
     * @param titleInformation the titleInformation to set
     */
    public void setTitleInformation(String titleInformation) {
        this.titleInformation = titleInformation;
    }

    /**
     * @return the totalInformation
     */
    public String getTotalInformation() {
        return totalInformation;
    }

    /**
     * @param totalInformation the totalInformation to set
     */
    public void setTotalInformation(String totalInformation) {
        this.totalInformation = totalInformation;
    }


    /**
     * @return the currentMonthInformation
     */
    public String getCurrentMonthInformation() {
        return currentMonthInformation;
    }

    /**
     * @param currentMonthInformation the currentMonthInformation to set
     */
    public void setCurrentMonthInformation(String currentMonthInformation) {
        this.currentMonthInformation = currentMonthInformation;
    }

    /**
     * @return the figureInformation
     */
    public String getFigureInformation() {
        return figureInformation;
    }

    /**
     * @param figureInformation the figureInformation to set
     */
    public void setFigureInformation(String figureInformation) {
        this.figureInformation = figureInformation;
    }

    /**
     * @return the totalValue
     */
    public String getTotalValue() {
        return totalValue;
    }

    /**
     * @param totalValue the totalValue to set
     */
    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }

    /**
     * @return the figureStatus
     */
    public String getFigureStatus() {
        return figureStatus;
    }

    /**
     * @param figureStatus the figureStatus to set
     */
    public void setFigureStatus(String figureStatus) {
        this.figureStatus = figureStatus;
    }



}
