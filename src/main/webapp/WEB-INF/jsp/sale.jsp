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
    <script type="text/javascript" src="/js/jquery-1.11.1.min.js"></script>
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

                    <li><a ><i class="fa fa-arrow-right"
                               aria-hidden="true"></i>Giá Cả</a></li>
                    <ul>
                        <li><a href="productfiter?product"><i class="fa fa-arrow-right" aria-hidden="true"></i>0 $ - 5 $</a></li>
                        <li><a href="filterprice010"><i class="fa fa-arrow-right" aria-hidden="true"></i>5 $ - 10 $</a>
                        </li>
                        <li><a href="filterprice1015"><i class="fa fa-arrow-right" aria-hidden="true"></i>10 $ - 15 $</a></li>
                        <li><a href="filterprice1520"><i class="fa fa-arrow-right" aria-hidden="true"></i>15 $ - 20 $</a>
                        </li>
                        <li><a href="filterprice20"><i class="fa fa-arrow-right" aria-hidden="true"></i>Trên 20 $</a></li>

                    </ul>
                    <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Grocery &
                        Staples</a></li>
                    <ul>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Dals &
                            Pulses</a></li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Dry
                            Fruits</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Edible Oils
                            &
                            Ghee</a></li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Flours &
                            Sooji</a></li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Masalas &
                            Spices</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Organic
                            Staples</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Rice & Rice
                            Products</a></li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Salt, Sugar
                            &
                            Jaggery</a></li>
                    </ul>
                    <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>PersonalCare</a>
                    </li>
                    <ul>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Baby
                            Care</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right"
                                                       aria-hidden="true"></i>Cosmetics</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Deos &
                            Perfumes</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Skin
                            Care</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Sanitary
                            Needs</a></li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Oral
                            Care</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Personal
                            Hygiene</a>
                        </li>
                        <li><a href="products.html"><i class="fa fa-arrow-right" aria-hidden="true"></i>Shaving
                            Needs</a></li>
                    </ul>
                </ul>
            </div>

        </div>
        <div class="col-md-8 products-right">
            <div class="products-right-grid">
                <div class="products-right-grids">
                    <div class="sorting">
                        <select id="country" onchange="change_country(this.value)" class="frm-field required sect">
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Default sorting
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Sort by popularity
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Sort by average
                                rating
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Sort by price
                            </option>
                        </select>
                    </div>
                    <div class="sorting-left">
                        <select id="country1" onchange="change_country(this.value)" class="frm-field required sect">
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Item on page 9
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Item on page 18
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>Item on page 32
                            </option>
                            <option value="null"><i class="fa fa-arrow-right" aria-hidden="true"></i>All</option>
                        </select>
                    </div>
                    <div class="clearfix"></div>
                </div>
            </div>

            <div class="agile_top_brands_grids">
                <c:forEach items="${sale}" var="productList">
                    <div class="col-md-4 top_brand_left">
                        <div class="hover14 column">
                            <div class="agile_top_brand_left_grid">
                                <div class="agile_top_brand_left_grid_pos">
                                    <img src="images/offer.png" alt=" " class="img-responsive">
                                </div>
                                <div class="agile_top_brand_left_grid1">
                                    <figure>
                                        <div class="snipcart-item block">
                                            <div class="snipcart-thumb">
                                                <a href="single.html"><img title=" " alt=" "
                                                                           src="/images/${productList.image}"/></a>
                                                <a href="/detail?id=${productList.id}"><p>${productList.name}</p></a>
                                                <h4>$35.99 <span>$55.00</span></h4>
                                            </div>
                                            <div class="snipcart-details top_brand_home_details">
                                                <form action="#" method="post">
                                                    <fieldset>
                                                        <input type="hidden" name="cmd" value="_cart">
                                                        <input type="hidden" name="add" value="1">
                                                        <input type="hidden" name="business" value=" ">
                                                        <input type="hidden" name="item_name"
                                                               value="Fortune Sunflower Oil">
                                                        <input type="hidden" name="amount" value="35.99">
                                                        <input type="hidden" name="discount_amount" value="1.00">
                                                        <input type="hidden" name="currency_code" value="USD">
                                                        <input type="hidden" name="return" value=" ">
                                                        <input type="hidden" name="cancel_return" value=" ">
                                                        <input type="submit" name="submit" value="Add to cart"
                                                               class="button">
                                                    </fieldset>
                                                </form>
                                            </div>
                                        </div>
                                    </figure>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>


                <nav class="numbering">
                    <ul class="pagination paging">
                        <li>
                            <a href="#" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                        <li class="active"><a href="#">1<span class="sr-only">(current)</span></a></li>
                        <li><a href="#">2</a></li>
                        <li><a href="#">3</a></li>
                        <li><a href="#">4</a></li>
                        <li><a href="#">5</a></li>
                        <li>
                            <a href="#" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
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
