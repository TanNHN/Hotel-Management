<%@include file="head.jsp"%>
<body>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <!--<script src=https://code.jquery.com/jquery-1.12.4.js></script>-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.9/additional-methods.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            if (window.location.href.indexOf("a") > -1) {
                if (searchParams.has('booked')) {
                    $("#checkInOut").show();
                    window.alert("f");
                }
            }
        });
    </script>
    <script>
        $(document).ready(function () {
            let searchParams = new URLSearchParams(window.location.search);
            if (searchParams.has('booked')) {
                $("#checkInOut").show();

            }
        });
    </script>
    <script>
        $(function () {
            $("#checkOutDatePicker").datepicker({
                dateFormat: "dd-mm-yy",
                minDate: new Date(),
                defaultDate: new Date()

            });
            $("#checkInDatePicker").datepicker({
                dateFormat: "dd-mm-yy",
                minDate: new Date(),
                defaultDate: new Date()
            });

        });
    </script>
    <!--    <script>
            function getStatus(sel) {
                if (sel.value === "avai") {
                    $("#checkInOut").hide();
    
                } else {
                    $("#checkInOut").show();
    
                }
            }
        </script>-->
    <h1>HOTEL's room</h1>

    <form action="MainController">
        Room type: <select name="slRoomTypeId" value="${param.slRoomTypeId}">
            <option ${param.slRoomTypeId == 0 ? 'selected' : ''} value="0">All</option>
        <c:forEach var="roomType" items="${requestScope.ROOM_TYPE_LIST}">
            <option ${param.slRoomTypeId == roomType.id ? 'selected' : ''} value="${roomType.id}">${roomType.name}</option>
        </c:forEach>
    </select><br>

<!--        Check in:  <input readonly="true" type="text" value="${requestScope.CHECK_IN}" id="checkInDatePicker" name="txtCheckIn"><br>
        Check out: <input readonly="true" type="text" value="${requestScope.CHECK_OUT}" id="checkOutDatePicker" name="txtCheckOut"><br>-->
        Check in: ${requestScope.CHECK_IN}<br>
        Check out: ${requestScope.CHECK_OUT}<br>
<!--        Page size: <select name="slPageSize">
            <option ${param.slPageSize == 20 ? 'selected' : ''} value="20">20</option>
            <option ${param.slPageSize == 50 ? 'selected' : ''} value="50">50</option>
            <option ${param.slPageSize == 100 ? 'selected' : ''} value="100">100</option>
        </select><br>-->
        <input type="hidden" name="hotelId" value="${requestScope.HOTEL_ID}">
        <input type="hidden" name="hotelName" value="${requestScope.HOTEL_NAME}">

        <input type="submit" name="action" value="Search room">
    </form>
    Total result: ${requestScope.TOTAL}<br>

    <c:if test="${not empty requestScope.INVALID_DATE}" var="notValid">
        ${requestScope.INVALID_DATE}
    </c:if>
    <c:if test="${!notValid}">
        <c:if test="${empty requestScope.ROOM_LIST}" var="noRoom">
            No room found
        </c:if>
        <c:if test="${!noRoom}">
            <table border="1" style="margin-top: 30px">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Room type</th>
                        <th>Price</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${requestScope.ROOM_LIST}" var="room">

                        <tr>
                            <td>${room.name}</td>
                            <td>${room.roomType.name}</td>
                            <td>${room.price}</td>

                            <td><a href="MainController?action=Add to cart&roomId=${room.id}&hotelId=${room.hotel.id}&txtCheckIn=${param.txtCheckIn}&txtCheckOut=${param.txtCheckOut}">Add to cart</a></td>

                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:if test="${not empty requestScope.ADD_TO_CART_SUCCESS_MESSAGE}">
                ${requestScope.ADD_TO_CART_SUCCESS_MESSAGE}
            </c:if>
            <c:if test="${not empty requestScope.EXIST_IN_CART_MESSAGE}">
                ${requestScope.EXIST_IN_CART_MESSAGE}
            </c:if>
            <c:set value="1" var="page"/>

            <c:if test="${param.slPageSize == null}" var="isStepNull">
                <c:set value="20" var="step"/>
            </c:if>
            <c:if test="${!isStepNull}">
                <c:set value="${param.slPageSize}" var="step"/>

            </c:if>
            <c:forEach begin="1" end="${requestScope.TOTAL}" step="${step}">

                <br><a href="MainController?action=Search room&hotelId=${param.hotelId}&slRoomTypeId=${param.slRoomTypeId}&txtCheckIn=${requestScope.CHECK_IN}&txtCheckOut=${requestScope.CHECK_OUT}&page=${page}&slPageSize=${requestScope.PAGE_SIZE}">${page}</a>  
                <c:set value="${page+1}" var="page"/>
            </c:forEach>
        </c:if>
    </c:if>


</body>
</html>
