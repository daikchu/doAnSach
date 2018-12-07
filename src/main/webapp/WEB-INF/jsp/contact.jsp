<%--
  Created by IntelliJ IDEA.
  User: VanTu
  Date: 03/18/18
  Time: 10:28 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

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


<div class="breadcrumbs">
    <div class="container">
        <ol class="breadcrumb breadcrumb1 animated wow slideInLeft" data-wow-delay=".5s">
            <li><a href="index.html"><span class="glyphicon glyphicon-home" aria-hidden="true"></span>Home</a></li>
            <li class="active">Contact</li>
        </ol>
    </div>
</div>
<!-- //breadcrumbs -->
<!-- contact -->
<div class="about">
    <div class="w3_agileits_contact_grids">
        <div class="col-md-6 w3_agileits_contact_grid_left">
            <div class="agile_map">
                <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d1150.8228211861567!2d108.1692781904578!3d16.075630272487793!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xcaff29dfb73f0ac!2zVHLGsOG7nW5nIMSQ4bqhaSBo4buNYyBGUFQ!5e0!3m2!1sen!2s!4v1507383834812" style="border:0"></iframe>
            </div>
            <div class="agileits_w3layouts_map_pos">
                <div class="agileits_w3layouts_map_pos1">
                    <h3>Contact Info</h3>
                    <p>137 Nguyễn Thị Thập, Hòa Minh, Liên Chiểu, Đà Nẵng, Vietnam.</p>
                    <ul class="wthree_contact_info_address">
                        <li><i class="fa fa-envelope" aria-hidden="true"></i><a href="mailto:info@example.com">info@example.com</a></li>
                        <li><i class="fa fa-phone" aria-hidden="true"></i>+(0123) 232 232</li>
                    </ul>
                    <div class="w3_agile_social_icons w3_agile_social_icons_contact">
                        <ul>
                            <li><a href="#" class="icon icon-cube agile_facebook"></a></li>
                            <li><a href="#" class="icon icon-cube agile_rss"></a></li>
                            <li><a href="#" class="icon icon-cube agile_t"></a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-6 w3_agileits_contact_grid_right">
            <h2 class="w3_agile_header">Leave a<span> Message</span></h2>

            <form action="#" method="post">
					<span class="input input--ichiro">
						<input class="input__field input__field--ichiro" type="text" id="input-25" name="Name" placeholder=" " required="" />
						<label class="input__label input__label--ichiro" for="input-25">
							<span class="input__label-content input__label-content--ichiro">Your Name</span>
						</label>
					</span>
                <span class="input input--ichiro">
						<input class="input__field input__field--ichiro" type="email" id="input-26" name="Email" placeholder=" " required="" />
						<label class="input__label input__label--ichiro" for="input-26">
							<span class="input__label-content input__label-content--ichiro">Your Email</span>
						</label>
					</span>
                <textarea name="Message" placeholder="Your message here..." required=""></textarea>
                <input type="submit" value="Submit">
            </form>
        </div>
        <div class="clearfix"> </div>
    </div>
</div>
<!-- contact -->

<jsp:include page="footer.jsp"></jsp:include>



<script src="js/bootstrap.min.js"></script>
<!-- top-header and slider -->
<!-- here stars scrolling icon -->
<script type="text/javascript">
    $(document).ready(function() {
        /*
            var defaults = {
            containerID: 'toTop', // fading element id
            containerHoverID: 'toTopHover', // fading element hover id
            scrollSpeed: 1200,
            easingType: 'linear'
            };
        */

        $().UItoTop({ easingType: 'easeOutQuart' });

    });
</script>
<!-- //here ends scrolling icon -->
<script src="js/minicart.min.js"></script>
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
<script src="js/skdslider.min.js"></script>
<link href="css/skdslider.css" rel="stylesheet">
<script type="text/javascript">
    jQuery(document).ready(function(){
        jQuery('#demo1').skdslider({'delay':5000, 'animationSpeed': 2000,'showNextPrev':true,'showPlayButton':true,'autoSlide':true,'animationType':'fading'});

        jQuery('#responsive').change(function(){
            $('#responsive_wrapper').width(jQuery(this).val());
        });

    });
</script>
<!-- //main slider-banner -->


</body>
</html>
