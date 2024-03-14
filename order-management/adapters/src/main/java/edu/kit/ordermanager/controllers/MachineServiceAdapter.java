package edu.kit.ordermanager.controllers;

import edu.kit.ordermanager.entities.Resource;
import edu.kit.ordermanager.entities.ResourceSet;
import edu.kit.ordermanager.handlers.IRestServiceHandler;
import edu.kit.pms.ordermanager.app.IMachineService;


public class MachineServiceAdapter implements IMachineService {

    IRestServiceHandler restServiceHandler;

    private String checkMachineUrl;

    private String requiredResourceUrl;

    private String startProductionUrl;

    private void initUrls() {

        checkMachineUrl = "http://" + System.getenv("MACHINE_HOST")+ ":" + System.getenv("MACHINE_PORT") + "/pms/mm/checkMachine";

        requiredResourceUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/requiredResource";

        startProductionUrl = "http://" + System.getenv("MACHINE_HOST") + ":" + System.getenv("MACHINE_PORT") + "/pms/mm/produce";
    }

    /**
     * For tests
     * @param restServiceHandler
     */
    public MachineServiceAdapter(IRestServiceHandler restServiceHandler) {
        this.restServiceHandler = restServiceHandler;
        initUrls();
    }


    @Override
    public boolean checkAvailableMachines(int resourceId) {
        String url = checkMachineUrl + "/" + resourceId;
        return Boolean.TRUE.equals(restServiceHandler.sendGetRequest(url, Boolean.class, null));
    }

    @Override
    public Resource findRequiredResource(int resourceId) {
        String url = requiredResourceUrl + "/" + resourceId;
        return restServiceHandler.sendGetRequest(url, Resource.class, null);
    }

    @Override
    public boolean startProduction(ResourceSet resourceSet) {
        return Boolean.TRUE.equals(restServiceHandler.sendPostRequest(startProductionUrl, resourceSet, Boolean.class));
    }
}
