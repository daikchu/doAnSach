<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: PC
  Date: 01/18/18
  Time: 2:02 CH
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="icon" type="image/png" href="/assets/img/favicon.ico">
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
</head>
<body>

<jsp:include page="header.jsp"></jsp:include>


        <div class="content">
            <div class="container-fluid">
                <div class="row">
                    <div class="col-md-12">
                        <div class="card">
                            <div class="header">
                                <h4 class="title">Quản Lý Danh Mục Sản Phẩm</h4>
                                <p class="category">Danh mục sản phẩm của cửa hàng</p>
                            </div>
                            <div class="content table-responsive table-full-width">

                                <table class="table table-hover table-striped">
                                    <thead>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Salary</th>
                                    <th>Action</th>
                                    <th>Action</th>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${categoryList}" var="categoryList">
                                        <tr>
                                            <td>${categoryList.id}</td>
                                            <td>${categoryList.name}</td>
                                            <td>${categoryList.nameVN}</td>
                                            <td>
                                                <a href="/admin/editCategory?id=${categoryList.id}"><img src="/assets/img/icon/edit.svg " height="20" width="20"></a></td>
                                            <td><a href="/delete/${categoryList.id}"><img src="/assets/img/icon/delete.svg " height="20"
                                                                                          width="20"></a></td>
                                        </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>


                            </div>
                        </div>
                    </div>


                    <div class="col-md-12">
                        <div class="card card-plain">
                            <div class="header">
                                <h4 class="title">Quản Lý Nhà Cung Cấp</h4>
                                <p class="category">Here is a subtitle for this table</p>
                            </div>
                            <div class="content table-responsive table-full-width">
                                <table class="table table-hover">
                                    <thead>
                                    <th>ID</th>
                                    <th>Email</th>
                                    <th>Logo</th>
                                    <th>Name</th>
                                    <th>Phone</th>
                                    <th>Action</th>
                                    <th>Action</th>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${supplierList}" var="supplierList">
                                        <tr>
                                            <td>${supplierList.id}</td>
                                            <td>${supplierList.email}</td>
                                            <td><img src="/resources/uploads/${supplierList.logo}" width="100"
                                                     height="50"/></td>
                                            <td>${supplierList.name}</td>
                                            <td>${supplierList.phone}</td>


                                            <td>
                                                <a href="/admin/editsuppliers?id=${supplierList.id}"><img src="/assets/img/icon/edit.svg " height="20" width="20"></a></td>
                                            <td><a href="/deletesup/${supplierList.id}"><img src="/assets/img/icon/delete.svg " height="20"
                                                                                             width="20"></a></td>


                                        </tr>
                                    </c:forEach>

                                    </tbody>
                                </table>

                            </div>
                        </div>
                    </div>


                </div>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>


    </div>
</div>
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
            message: "${message}"

        }, {
            type: 'info',
            timer: 4000
        });

    });
</script>
</body>
</html>
