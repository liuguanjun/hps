<%@ include file="/common/taglibs.jsp"%>

<menu:useMenuDisplayer name="Velocity" config="navbarMenu.vm" permissions="rolesAdapter">
<div class="collapse navbar-collapse" id="navbar">
<ul class="nav navbar-nav">
    <c:if test="${empty pageContext.request.remoteUser}">
        <li class="active">
            <a href="<c:url value="/login"/>"><fmt:message key="login.title"/></a>
        </li>
    </c:if>
<%--     <menu:displayMenu name="Home"/> --%>
<%--     <menu:displayMenu name="UserMenu"/> --%>
    <menu:displayMenu name="AdminMenu"/>
    <menu:displayMenu name="FangwuZongheChaxun"/>
    <menu:displayMenu name="ElectricCharge"/>
    <menu:displayMenu name="ElectricChaobiao"/>
    <menu:displayMenu name="QunuanfeiCharge"/>
    <menu:displayMenu name="HouseRepairCharge"/>
    <menu:displayMenu name="ElectricTongji"/>
    <menu:displayMenu name="QunuanfeiTongji"/>
    <menu:displayMenu name="WeixiufeiTongji"/>
    
<%--     <menu:displayMenu name="BaseDataManage"/> --%>
    <menu:displayMenu name="Logout"/>
<%--     <menu:displayMenu name="AppFuseSample"/> --%>
</ul>
</div>
</menu:useMenuDisplayer>
