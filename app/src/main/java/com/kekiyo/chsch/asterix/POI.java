package com.kekiyo.chsch.asterix;

/**
 * Created by CSean on 17.11.2016.
 */

/*
* {
"type": "fuel",
"name": "Esso",
"address": "Frankfurter Straße 65",
"lat": 49.8848387,
"lon": 8.6520691 },
{
"type": "amenity",
"name": "HoffART Theater",
"address": "Bennelbächerweg",
"lat": 49.8784609,
"lon": 8.6593199,
"icon": "hoffart.jpg" },
{
"type": "fuel",
"name": "Total",
"address": "Siemensstraße 2",
"lat": 49.8959045,
"lon": 8.6809781 },
{
"type": "pub",
"name": "Cluster",
"address": "Wilhelm-Leuschner-Straße 48",
"lat": 49.8801245,
"lon": 8.6453214,
"capacity": 130 },
{
"type": "pub",
"name": "Parliament of Rock",
"address": "Mauerstraße 20",
"lat": 49.87694,
"lon": 8.6594569,
"capacity": 60 },
{
"type": "pub",
"name": "Bangertseck",
"address": "Barkhausstraße 2",
"lat": 49.8824086,
"lon": 8.6594886,
"capacity": 45 },
{
"type": "pub",
"name": "Cafe Tornado",
"address": "Mollerstadt Saalbaustraße",
"lat": 49.8715443,
"lon": 8.6470329,
"capacity": 55 },
{
"type": "pub",
"name": "Unikum",
"address": "Mollerstadt Saalbaustraße",
"lat": 49.8714409,
"lon": 8.647085,
"capacity": 90 }
*
* */

public class POI {
    String type;
    String name;
    String address;
    double latitude;
    double longitude;
    //amenity specific
    String icon;
    //pub specific
    int capacity;
}

