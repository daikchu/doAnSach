<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01/18/18
  Time: 12:09 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="wrapper">
    <div class="sidebar" data-color="purple" data-image="/assets/img/sidebar-5.jpg">

        <!--   you can change the color of the sidebar using: data-color="blue | azure | green | orange | red | purple" -->


        <div class="sidebar-wrapper">
            <div class="logo">
                <a  class="simple-text">
                    Yumi Shop
                </a>
            </div>

            <ul class="nav">
                <li>
                    <a href="/admin/dashboard">
                        <i class="pe-7s-graph"></i>
                        <p>Dashboard</p>
                    </a>
                </li>
                <li class="active">
                    <a href="/admin/addcategory">
                        <i class="pe-7s-user"></i>
                        <p>Thêm Danh Mục</p>
                    </a>
                </li>
                <li>
                    <a href="/admin/tablelist">
                        <i class="pe-7s-note2"></i>
                        <p>Category & Suppliers</p>
                    </a>
                </li>
                <li>
                    <a href="/admin/addproduct">
                        <i class="pe-7s-news-paper"></i>
                        <p>Addproduct</p>
                    </a>
                </li>
                <li>
                    <a href="/admin/listproduct">
                        <i class="pe-7s-science"></i>
                        <p>List Product</p>
                    </a>
                </li>
                <li>
                    <a href="/admin/addsuppliers">
                        <i class="pe-7s-map-marker"></i>
                        <p>Add Suppliers</p>
                    </a>
                </li>
                <li>
                    <a href="/admin/order">
                        <i class="pe-7s-bell"></i>
                        <p>Đơn Hàng Mới</p>
                    </a>
                </li>
                <li class="active-pro">
                    <a href="upgrade.html">
                        <i class="pe-7s-rocket"></i>
                        <p>Upgrade to PRO</p>
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <div class="main-panel">
        <nav class="navbar navbar-default navbar-fixed">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navigation-example-2">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">User</a>
                </div>
                <div class="collapse navbar-collapse">
                    <ul class="nav navbar-nav navbar-left">
                        <li>
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-dashboard"></i>
                                <p class="hidden-lg hidden-md">Dashboard</p>
                            </a>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-globe"></i>
                                <b class="caret hidden-sm hidden-xs"></b>
                                <span class="notification hidden-sm hidden-xs">5</span>
                                <p class="hidden-lg hidden-md">
                                    5 Notifications
                                    <b class="caret"></b>
                                </p>
                            </a>
                            <ul class="dropdown-menu">
                            <c:forEach items="${thongbaorList}" var="thongbaorList">
                                <li><a href="/admin/orderlist?id=${thongbaorList.id}">${thongbaorList.id}</a></li>
                            </c:forEach>
                            </ul>
                        </li>
                        <li>
                            <div class="w3l_search">
                                <form action="/header" method="post">
                                    <input type="search" name="name" placeholder="Search for a Product..." required id="input-search">
                                    <button type="submit" class="btn btn-default search" aria-label="Left Align">
                                        <i class="fa fa-search" aria-hidden="true"> </i>
                                    </button>
                                    <div class="clearfix"></div>
                                </form>
                            </div>
                        </li>
                    </ul>

                    <ul class="nav navbar-nav navbar-right">
                        <li>
                            <a href="">
                                <p>Account</p>
                            </a>
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                                <p>
                                    Dropdown
                                    <b class="caret"></b>
                                </p>

                            </a>
                            <ul class="dropdown-menu">
                                <li><a href="/admin/report">Thống Kê Theo Sản Phẩm</a></li>
                                <li><a href="/admin/reportcategory">Thống Kê Theo Danh Mục</a></li>
                                <li><a href="/admin/reportsuppliers">Thống Kê Theo Hãng</a></li>
                                <li><a href="/admin/reportordercustomer">Thống Kê Theo Người Đặt Hàng</a></li>
                                <li><a href="/admin/reportyear">Thống Kê Theo Năm</a></li>
                                <li><a href="/admin/reportmonth">Thống Kê Theo Tháng</a></li>
                                <li><a href="/admin/reportquarter">Thống Kê Theo Quý</a></li>
                            </ul>
                        </li>
                        <li>
                            <a href="/logout">
                                <p>Log out</p>
                            </a>
                        </li>
                        <li class="separator hidden-lg hidden-md"></li>
                    </ul>
                </div>
            </div>
        </nav>

</body>
</html>
