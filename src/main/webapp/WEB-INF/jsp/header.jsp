<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01/17/18
  Time: 7:10 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="icon" href="http://getbootstrap.com/favicon.ico">

    <!-- Bootstrap -->


    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="/js/jquery.autocomplete.min.js"></script>

    <script>
        $(document).ready(function () {
            $('#input-search').autocomplete({
                serviceUrl: '${pageContext.request.contextPath}/header',
                paramName: "name",
                delimiter: ",",
                transformResult: function (response) {
                    return {
                        //must convert json to javascript object before process
                        suggestions: $.map($.parseJSON(response), function (item) {
                            return {value: item.name, data: item.id};
                        })
                    };
                }
            });
        });
    </script>


</head>
<body>
<!-- header -->
<div class="agileits_header">
    <div class="container">
        <div class="w3l_offers">
            <p>SALE UP TO 70% OFF. USE CODE "SALE70%" . <a href="/product">SHOP NOW</a></p>
        </div>
        <div class="agile-login">
            <ul>
                <c:if test="${id == null}">
                        <li><a href="/registered"><spring:message code="create" text="default text"/> </a></li>
                        <li><a href="/login"><spring:message code="login" text="default text"/></a>

                        <%--<a class="dropdown-toggle" data-toggle="dropdown">--%>
                            <%--<i class="fa fa-user" aria-hidden="true"></i> My Account--%>
                            <%--<span class="caret"></span>--%>
                        <%--</a>--%>
                    </c:if>
                    <c:if test="${id != null}">
                    <a href="" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-user" aria-hidden="true"></i> Hi ${id}
                    </a>
                    <li><a href="listordercus?customerId=${id}">Đơn Hàng</a></li>

                    <li><a href="productbycustome?customerId=${id}">Sản Phẩm Đã Mua</a></li>
                <li><a href="/logout">Log Out</a></li>
                </c:if>
                <ul class="dropdown-menu">
                    <li><a href="/login">Login </a></li>
                    <li><a href="signup.html">Sign Up</a></li>
                    <li><a href="login.jsp">My Orders</a></li>
                    <li><a href="login.jsp">Wallet</a></li>
                </ul>
                </li>
                <li><a href="contact.html"></a></li>


                <a href="?language=en"> <img src="/images/england.svg" width="20" height="20"/></a>
                <a href="?language=vi_VN "> <img src="/images/vietnam.svg" width="20" height="20"/></a>
                <a href="?language=ja_JP "> <img src="/images/japan.svg" width="20" height="20"/></a>

            </ul>
        </div>

        <div class="clearfix"></div>
    </div>
</div>

<div class="logo_products">
    <div class="container">
        <div class="w3ls_logo_products_left1">
            <ul class="phone_email">
                <li><i class="fa fa-phone" aria-hidden="true"></i>Order online or call us : (+84) 976 704 644</li>

            </ul>
        </div>
        <div class="w3ls_logo_products_left">
            <h1><a href="/">Yumi Shop</a></h1>
        </div>
        <div class="w3l_search">
            <form action="/header" method="post">
                <input type="search" name="name" placeholder="Search for a Product..." required id="input-search">
                <button type="submit" class="btn btn-default search" aria-label="Left Align">
                    <i class="fa fa-search" aria-hidden="true"> </i>
                </button>
                <div class="clearfix"></div>
            </form>
        </div>

        <div class="clearfix"></div>
    </div>
</div>
<!-- //header -->
<!-- navigation -->
<div class="navigation-agileits">
    <div class="container">
        <nav class="navbar navbar-default">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header nav_2">
                <button type="button" class="navbar-toggle collapsed navbar-toggle1" data-toggle="collapse"
                        data-target="#bs-megadropdown-tabs">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
            </div>

            <div class="collapse navbar-collapse" id="bs-megadropdown-tabs">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="/" class="act"><spring:message code="index" text="default text"/></a>
                    </li>
                    <!-- Mega Menu -->
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="hello" text="default text"/><b class="caret"></b></a>

                        <ul class="dropdown-menu multi-column columns-3">
                            <h6>All Household</h6>
                            <c:forEach items="${categoryList}" var="categoryList">
                                <div class="row">

                                    <div class="multi-gd-img">

                                        <ul class="multi-column-dropdown">

                                            <li>
                                                <a href="productbycategory?categoryId=${categoryList.id}">${categoryList.name}</a>
                                            </li>

                                        </ul>

                                    </div>


                                </div>
                            </c:forEach>

                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><spring:message code="supplier" text="default text"/><b
                                class="caret"></b></a>
                        <ul class="dropdown-menu multi-column columns-3">
                            <h6>All Household</h6>
                            <c:forEach items="${supplierList}" var="supplierList">

                                <div class="row">

                                    <div class="multi-gd-img">

                                        <ul class="multi-column-dropdown">

                                            <li>
                                                <a href="productbysupplier?supplierId=${supplierList.id}" >${supplierList.name}</a>
                                            </li>

                                        </ul>
                                    </div>


                                </div>
                            </c:forEach>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="/product"><spring:message code="sale" text="default text"/></a>

                    </li>
                    <li class="dropdown">
                        <a href="/searchOrder" ><spring:message code="check" text="default text"/></a>
                    </li>
                    <li><a href="/viewCart"><spring:message code="cart" text="default text"/></a></li>
                    <li><a href="/contact"><spring:message code="contact" text="default text"/></a></li>


                </ul>
            </div>
        </nav>
    </div>
</div>
</body>
</html>
