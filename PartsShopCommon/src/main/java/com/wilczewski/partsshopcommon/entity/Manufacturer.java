package com.wilczewski.partsshopcommon.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "manufacturers")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private String alias;

    @Column(nullable = false)
    private String image;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Manufacturer parent;

    @OneToMany(mappedBy = "parent")
    private Set<Manufacturer> children = new HashSet<>();

    public Manufacturer() {
    }

    public Manufacturer(Integer id) {
        this.id = id;
    }

    public Manufacturer(Integer id, String name, String alias) {
        this.id = id;
        this.name = name;
        this.alias = alias;
    }

    public Manufacturer(String name) {
        this.name = name;
        this.alias = name;
        this.image ="default";
    }

    public Manufacturer(String name, Manufacturer parent) {
        this(name);
        this.parent = parent;
    }

    public static Manufacturer copyIdAndName(Manufacturer manufacturer){
        Manufacturer copyManufacturer = new Manufacturer();
        copyManufacturer.setId(manufacturer.getId());
        copyManufacturer.setName(manufacturer.getName());

        return copyManufacturer;
    }

    public static Manufacturer copyIdAndName(Integer id, String name){
        Manufacturer copyManufacturer = new Manufacturer();
        copyManufacturer.setId(id);
        copyManufacturer.setName(name);

        return copyManufacturer;
    }

    public static Manufacturer copyFull(Manufacturer manufacturer){
        Manufacturer copyManufacturer = new Manufacturer();
        copyManufacturer.setId(manufacturer.getId());
        copyManufacturer.setName(manufacturer.getName());
        copyManufacturer.setImage(manufacturer.getImage());
        copyManufacturer.setAlias(manufacturer.getAlias());
        copyManufacturer.setHasChildren(manufacturer.getChildren().size() > 0);

        return copyManufacturer;
    }

    public static Manufacturer copyFull(Manufacturer manufacturer, String name){
        Manufacturer copyManufacturer = Manufacturer.copyFull(manufacturer);
        copyManufacturer.setName(name);

        return copyManufacturer;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Manufacturer getParent() {
        return parent;
    }

    public void setParent(Manufacturer parent) {
        this.parent = parent;
    }

    public Set<Manufacturer> getChildren() {
        return children;
    }

    public void setChildren(Set<Manufacturer> children) {
        this.children = children;
    }

    @Transient
    public String getImagePath(){
        if (this.id == null) return "/images/image-thumbnail.png";
        return "/manufacturer-images/" + this.id + "/" + this.image;
    }

    public  boolean isHasChildren(){
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren){
        this.hasChildren = hasChildren;
    }

    @Transient
    private boolean hasChildren;

    @Override
    public String toString() {
        return this.name;
    }

}
