<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01/19/18
  Time: 4:30 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Ping Shop</title>


    <script src="/js/jquery.3.1.1.min.js"></script>
    <script src="/js/jquery.twbsPagination.min.js"/>

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <!-- for-mobile-apps -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="keywords" content="Super Market Responsive web template, Bootstrap Web Templates, Flat Web Templates, Android Compatible web template,
Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyEricsson, Motorola web design"/>
    <!-- //for-mobile-apps -->
    <link href="/css/bootstrap1.css" rel="stylesheet" type="text/css" media="all"/>
    <link href="/css/style.css" rel="stylesheet" type="text/css" media="all"/>
    <!-- font-awesome icons -->
    <link href="/css/font-awesome.css" rel="stylesheet">
    <!-- //font-awesome icons -->
    <!-- js -->
    <%--<script type="text/javascript" src="/resources/js/jquery-1.11.1.min.js"></script>--%>
    <!-- //js -->
    <!-- start-smoth-scrolling -->
    <script type="text/javascript" src="/js/move-top.js"></script>
    <script type="text/javascript" src="/js/easing.js"></script>
    <script type="text/javascript">
        jQuery(document).ready(function ($) {
            $(".scroll").click(function (event) {
                event.preventDefault();
                $('html,body').animate({scrollTop: $(this.hash).offset().top}, 1000);
            });

            $('#pagination').twbsPagination({
                totalPages: parseInt(${totalPages}),
                visiblePages: parseInt(${visiblePages}),
                initiateStartPageClick: false,
                startPage: parseInt(${startPage}),
                onPageClick: function (event, page) {
                    window.location.href = "?page=" + page;
                }
            });
        });
    </script>
    <!-- start-smoth-scrolling -->
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<!-- //breadcrumbs -->
<!--- gourmet --->
<div class="products">
    <div class="container">
        <div class="col-md-4 products-left">

            <div class="categories">
                <h2>Tìm Kiếm</h2>
                <ul class="cate">

                    <li><a><i class="fa fa-arrow-right"
                              aria-hidden="true"></i>Giá Cả</a></li>
                    <ul>
                        <li><a href="productfiter?product"><i class="fa fa-arrow-right" aria-hidden="true"></i>0 $ - 5 $</a>
                        </li>
                        <li><a href="filterprice010"><i class="fa fa-arrow-right" aria-hidden="true"></i>5 $ - 10 $</a>
                        </li>
                        <li><a href="filterprice1015"><i class="fa fa-arrow-right" aria-hidden="true"></i>10 $ - 15
                            $</a></li>
                        <li><a href="filterprice1520"><i class="fa fa-arrow-right" aria-hidden="true"></i>15 $ - 20
                            $</a>
                        </li>
                        <li><a href="filterprice20"><i class="fa fa-arrow-right" aria-hidden="true"></i>Trên 20 $</a>
                        </li>

                    </ul>
                    <li><a><i class="fa fa-arrow-right" aria-hidden="true"></i>Danh Mục Sản Phẩm</a></li>
                    <ul>

                        <c:forEach items="${categoryList}" var="categoryList">
                            <li><a href="productbycategory?categoryId=${categoryList.id}"><i class="fa fa-arrow-right"
                                                           aria-hidden="true"></i>${categoryList.name}</a></li>

                        </c:forEach>

                    </ul>
                    <li><a ><i class="fa fa-arrow-right" aria-hidden="true"></i>Nhà Cung Cấp</a>
                    </li>
                    <ul>
                        <c:forEach items="${supplierList}" var="supplierList">
                        <li><a href="productbysupplier?supplierId=${supplierList.id}"><i class="fa fa-arrow-right" aria-hidden="true"></i>${supplierList.name}</a>
                        </li>
                        </c:forEach>

                    </ul>
                </ul>
            </div>

        </div>
        <div class="col-md-8 products-right">
            <div class="products-right-grid">
                <div class="products-right-grids">


                    <div class="clearfix"></div>
                </div>
            </div>

            <div class="agile_top_brands_grids">
                <c:forEach items="${productList}" var="productList">
                    <div class="col-md-4 top_brand_left">
                        <div class="hover14 column">
                            <div class="agile_top_brand_left_grid">
                                <div class="agile_top_brand_left_grid_pos">
                                    <img src="/images/offer.png" alt=" " class="img-responsive">
                                </div>
                                <div class="agile_top_brand_left_grid1">
                                    <figure>
                                        <div class="snipcart-item block">
                                            <div class="snipcart-thumb">
                                                <a href="/detail?id=${productList.id}"><img title=" " alt=" "
                                                                                            src="/images/${productList.image}"/></a>
                                                <a href="/detail?id=${productList.id}"><p>${productList.name}</p></a>
                                                <h4>${productList.unitPrice} $ <span></span></h4>
                                            </div>
                                            <div class="snipcart-details top_brand_home_details">
                                                <a href="/addToCart?id=${productList.id}">
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

                <nav aria-label="Page navigation">
                    <ul class="pagination" style="margin-left: 90px;" id="pagination">
                    </ul>
                </nav>
            </div>

            <div class="clearfix"></div>

        </div>


    </div>


    <!--- gourmet --->
    <!-- //footer -->

    <jsp:include page="footer.jsp"></jsp:include>


    <!-- Bootstrap Core JavaScript -->
    <script src="/js/bootstrap.min.js"></script>

    <!-- top-header and slider -->
    <!-- here stars scrolling icon -->
    <script type="text/javascript">
        $(document).ready(function () {
            /*
                var defaults = {
                containerID: 'toTop', // fading element id
                containerHoverID: 'toTopHover', // fading element hover id
                scrollSpeed: 1200,
                easingType: 'linear'
                };
            */

            $().UItoTop({easingType: 'easeOutQuart'});

        });
    </script>
    <!-- //here ends scrolling icon -->
    <script src="/js/minicart.min.js"></script>
    <script>
        // Mini Cart
        paypal.minicart.render({
            action: '#'
        });

        if (~window.location.search.indexOf('reset=true')) {
            paypal.minicart.reset();
        }
    </script>
    <!-- main slider-banner -->
    <script src="/js/skdslider.min.js"></script>
    <link href="/css/skdslider.css" rel="stylesheet">
    <script type="text/javascript">
        jQuery(document).ready(function () {
            jQuery('#demo1').skdslider({
                'delay': 5000,
                'animationSpeed': 2000,
                'showNextPrev': true,
                'showPlayButton': true,
                'autoSlide': true,
                'animationType': 'fading'
            });

            jQuery('#responsive').change(function () {
                $('#responsive_wrapper').width(jQuery(this).val());
            });

        });
    </script>
    <!-- //main slider-banner -->
</body>
</html>
