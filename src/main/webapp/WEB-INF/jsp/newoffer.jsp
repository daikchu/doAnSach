<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: VanTu
  Date: 1/30/2018
  Time: 4:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div class="newproducts-w3agile">
    <div class="container">
        <h3>New offers</h3>
        <div class="agile_top_brands_grids">
            <c:forEach items="${listTest1}" var="report">
                <div class="col-md-3 top_brand_left-1">
                    <div class="hover14 column">
                        <div class="agile_top_brand_left_grid">
                            <div class="agile_top_brand_left_grid_pos">
                                <img src="/images/offer.png" alt=" " class="img-responsive">
                            </div>
                            <div class="agile_top_brand_left_grid1">
                                <figure>
                                    <div class="snipcart-item block">
                                        <div class="snipcart-thumb">
                                            <a href="products.html"><img title=" " alt=" " src="/images/${report[1]}"></a>
                                            <p><a href="/detail?id=${report[2]}">${report[0]}</a></p>
                                            <div class="stars">
                                                <i class="fa fa-star blue-star" aria-hidden="true"></i>
                                                <i class="fa fa-star blue-star" aria-hidden="true"></i>
                                                <i class="fa fa-star blue-star" aria-hidden="true"></i>
                                                <i class="fa fa-star blue-star" aria-hidden="true"></i>
                                                <i class="fa fa-star gray-star" aria-hidden="true"></i>
                                            </div>
                                            <h4>${report[4]} $ <span></span></h4>
                                        </div>
                                        <div class="snipcart-details top_brand_home_details">
                                            <a href="/addToCart?id=${report[2]}">
                                                <img src="/images/cart.svg" width="50"
                                                     height="50" alt=" " class="img-responsive"/></a>
                                        </div>
                                    </div>
                                </figure>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>

            <div class="clearfix"></div>
        </div>
    </div>
</div>
</body>
</html>
