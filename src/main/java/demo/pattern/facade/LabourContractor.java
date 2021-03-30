package demo.pattern.facade;

import demo.pattern.facade.subclass.BrickLayer;
import demo.pattern.facade.subclass.BrickWorker;
import demo.pattern.facade.subclass.Mason;

public class LabourContractor {

    private BrickLayer worker1 = new BrickLayer();

    private BrickWorker worker2 = new BrickWorker();

    private Mason worker3 = new Mason();

    public void buildHouse() {
        worker1.neat();
        worker2.carry();
        worker3.mix();
    }


}
