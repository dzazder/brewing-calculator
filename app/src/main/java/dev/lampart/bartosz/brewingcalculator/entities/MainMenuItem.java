package dev.lampart.bartosz.brewingcalculator.entities;

import dev.lampart.bartosz.brewingcalculator.calculators.Calc;

/**
 * Created by bartek on 03.07.2016.
 */
public class MainMenuItem {
    private int id;
    private String title;
    private String description;
    private String icon;
    private int fragment;

    public MainMenuItem() {
    }

    public MainMenuItem(int id, String title, String description, String icon, int fragment) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.fragment = fragment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }
}
