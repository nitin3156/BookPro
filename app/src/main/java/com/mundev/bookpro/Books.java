package com.mundev.bookpro;

/**
 * Created by Nitin Tonger on 14-03-2017.
 */

public class Books {
    public String product ,imgurl,barcode;

    public String price,id;
    public Books(String product,String barcode,String imgurl,String price,String id)
    {
        this.id=id;
        this.product=product;
        this.price=price;
        this.barcode=barcode;
        this.imgurl=imgurl;
    }
    public String  getId()
    {
        return id;
    }
    public void setId()
    {
        this.id=id;
    }
    public String  getproduct()
    {
        return product;
    }
    public void setProduct(String product)
    {
        this.product=product;
    }
    public String  getBarcode()
    {
        return barcode;
    }
    public  void setBarcode(String  barcode)
    {
        this.barcode=barcode;
    }
    public  String getImgurl()
    {
        return imgurl;
    }
    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
    public String  getPrice()
    {
        return  price;
    }
    public void setPrice(String price)
    {
        this.price=price;
    }
}
