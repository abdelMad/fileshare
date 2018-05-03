<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="sidebar" class="sidebar                  responsive">
    <ul class="nav nav-list">
        <li class="">
            <a href="/">
                <i class="menu-icon fa fa-tachometer"></i>
                <span class="menu-text"> Accueil </span>
            </a>

            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-file-o"></i>
                <span class="menu-text"> Documents </span>

                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu nav-hide" style="display: none;">
                <li class="">
                    <a href="/mes-documents">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Mes Documents
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="/documents-favoris">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Favoris
                    </a>

                    <b class="arrow"></b>
                </li>
                <li class="">
                    <a href="/historique">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Historique
                    </a>

                    <b class="arrow"></b>
                </li>

            </ul>
        </li>
        <li class="">
            <a href="/nouveau-document">
                <i class="menu-icon fa fa-pencil-square-o"></i>
                <span class="menu-text"> Nouveau document </span>
            </a>

            <b class="arrow"></b>
        </li>
        <li class="">
            <a href="/deconnexion">
                <i class="menu-icon fa fa-power-off"></i>
                <span class="menu-text"> Deconnexion </span>
            </a>

            <b class="arrow"></b>
        </li>

    </ul><!-- /.nav-list -->

    <div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
        <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
    </div>

</div>