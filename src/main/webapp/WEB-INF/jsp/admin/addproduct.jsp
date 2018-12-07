<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="select" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <link rel="icon" type="image/png" href="/resources/assets/img/favicon.ico">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>

    <title>Light Bootstrap Dashboard by Creative Tim</title>

    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport'/>
    <meta name="viewport" content="width=device-width"/>


    <!-- Bootstrap core CSS     -->
    <link href="/assets/css/bootstrap.min.css" rel="stylesheet"/>

    <!-- Animation library for notifications   -->
    <link href="/assets/css/animate.min.css" rel="stylesheet"/>

    <!--  Light Bootstrap Table core CSS    -->
    <link href="/assets/css/light-bootstrap-dashboard.css?v=1.4.0" rel="stylesheet"/>


    <!--  CSS for Demo Purpose, don't include it in your project     -->
    <link href="/assets/css/demo.css" rel="stylesheet"/>


    <!--     Fonts and icons     -->
    <link href="http://maxcdn.bootstrapcdn.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
    <link href='http://fonts.googleapis.com/css?family=Roboto:400,700,300' rel='stylesheet' type='text/css'>
    <link href="/assets/css/pe-icon-7-stroke.css" rel="stylesheet"/>
    <script src=/assets/js/jquery.validate.min.js"></script>


</head>
<body>
<jsp:include page="header.jsp"></jsp:include>

<div class="content">
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-8">
                <div class="card">
                    <div class="header">
                        <h4 class="title">Add Product</h4>
                    </div>
                    <div class="content">
                        ${message}
                        <form:form action="/admin/addproduct" method="post" modelAttribute="product"
                                   enctype="multipart/form-data" id="productadmin">
                            <div class="row">
                                <div class="col-md-5">
                                    <div class="form-group">
                                        <label>Tên Hàng</label>
                                        <form:input path="name" class="form-control"></form:input>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="form-group">
                                        <label>Giá

                                        </label>
                                        <form:input path="unitPrice" class="form-control" required="true"></form:input>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Đơn vị tính</label>
                                        <form:input path="unitBrief" class="form-control"></form:input>
                                    </div>
                                </div>
                            </div>


                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>
                                            Giảm giá</label>
                                        <form:input type="number" path="discount" class="form-control"
                                                    min="0"></form:input>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label>Số lượng</label>
                                        <form:input type="number" path="quantity" class="form-control"
                                                    min="0"></form:input>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>
                                            Ngày nhập </label>
                                        <form:input type="date" path="productDate" class="form-control"
                                                    required="true"></form:input>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Nhà Cung Cấp</label>
                                        <form:select items="${supplierList}" itemLabel="name"
                                                     itemValue="id" path="supplier.id" class="form-control">

                                        </form:select>

                                    </div>
                                </div>
                                <div class="col-md-4">

                                    <div class="form-group">
                                        <label>Loại</label>
                                        <form:select items="${categoryList}" itemLabel="name"
                                                     itemValue="id" path="category.id" class="form-control">

                                        </form:select>


                                    </div>

                                </div>
                                <div class="col-md-4">
                                    <div class="form-group">
                                        <label>Hình Ảnh</label>
                                        <input name="file" type="file" class="form-control" required="true">
                                    </div>
                                </div>
                            </div>


                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>Mô Tả</label>
                                        <form:input path="description" class="form-control"></form:input>
                                    </div>
                                </div>
                            </div>
                            <%--<div class="row">--%>
                            <%--<div class="col-md-12">--%>
                            <%--<div class="form-group">--%>
                            <%--<label>giá</label>--%>
                            <%--<form:input path="unitPrice" class="form-control" required="true"></form:input>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--</div>--%>

                            <button type="submit" class="btn btn-info btn-fill pull-right">Update Profile</button>
                            <div class="clearfix"></div>
                        </form:form>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card card-user">
                    <div class="image">
                        <img src="https://ununsplash.imgix.net/photo-1431578500526-4d9613015464?fit=crop&fm=jpg&h=300&q=75&w=400"
                             alt="..."/>
                    </div>
                    <div class="content">
                        <div class="author">
                            <a href="#">
                                <img class="avatar border-gray" src="/resources/assets/img/faces/admintu.jpg"
                                     alt="..."/>

                                <h4 class="title">Mike Andrew<br/>
                                    <small>michael24</small>
                                </h4>
                            </a>
                        </div>

                        <c:if test="${id != null}">
                            <p class="description text-center"> "Hi ${id} <br>
                                Your chick she so thirsty <br>
                                I'm in that two seat Lambo"
                            </p>
                        </c:if>
                    </div>
                    <hr>
                    <div class="text-center">
                        <button href="#" class="btn btn-simple"><i class="fa fa-facebook-square"></i></button>
                        <button href="#" class="btn btn-simple"><i class="fa fa-twitter"></i></button>
                        <button href="#" class="btn btn-simple"><i class="fa fa-google-plus-square"></i></button>

                    </div>
                </div>
            </div>

        </div>
    </div>
</div>


<jsp:include page="footer.jsp"></jsp:include>

</div>
</div>


</body>

<!--   Core JS Files   -->
<script src="/assets/js/jquery.3.2.1.min.js" type="text/javascript"></script>
<script src="/assets/js/bootstrap.min.js" type="text/javascript"></script>

<!--  Charts Plugin -->
<script src="/assets/js/chartist.min.js"></script>

<!--  Notifications Plugin    -->
<script src="/assets/js/bootstrap-notify.js"></script>

<!--  Google Maps Plugin    -->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=YOUR_KEY_HERE"></script>

<!-- Light Bootstrap Table Core javascript and methods for Demo purpose -->
<script src="/assets/js/light-bootstrap-dashboard.js?v=1.4.0"></script>

<!-- Light Bootstrap Table DEMO methods, don't include it in your project! -->
<script src="/assets/js/demo.js"></script>

<script type="text/javascript">
    $(document).ready(function () {

        demo.initChartist();

        $.notify({
            icon: 'pe-7s-gift',
            message: "Chào Mừng Bạn <b>Đến Với Trang Thống Kê</b> Chúc cửa hàng bạn luôn thành công."

        }, {
            type: 'info',
            timer: 4000
        });

    });
</script>

</html>
