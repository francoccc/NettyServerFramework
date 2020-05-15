package com.franco.spring.view;


import java.util.HashMap;

/**
 * ViewManager
 *
 * @author franco
 */
public class ViewManager {

    private ResponseView defaultView;

    private HashMap<String, ResponseView> viewMap = new HashMap<String, ResponseView>();

    public ViewManager(ResponseView defaultView) {
        this.defaultView = defaultView;
    }

    public void addView(String viewName, ResponseView view){
        viewMap.put(viewName, view);
    }

    public ResponseView getView(String viewName) {
        return viewMap.get(viewName);
    }
}
