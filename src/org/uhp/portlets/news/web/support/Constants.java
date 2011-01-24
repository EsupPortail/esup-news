/**
 * @Project NewsPortlet : http://sourcesup.cru.fr/newsportlet/ 
 * Copyright (C) 2007-2008 University Nancy 1
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.uhp.portlets.news.web.support;

public final class Constants {
    public static final String ACT = "action";

    public static final String ACT_ADD_ENTITY = "entityAdd";
    public static final String ACT_ADD_AUDIENCE = "addAudience";
    public static final String ACT_ADD_PERM = "addPermission";
    public static final String ACT_ADD_CAT = "categoryAdd";
    public static final String ACT_ADD_ITEM = "itemAdd";
    public static final String ACT_ADD_SUPERADMIN = "addSuperUser";
    public static final String ACT_ADD_TOPIC = "topicAdd";
    public static final String ACT_ADD_TYPE = "typeAdd";
    public static final String ACT_ADD_FILTER = "filterAdd";

    public static final String ACT_ADD_EXTERNAL_ATTACHMENT = "externalAttachmentAdd";
    public static final String ACT_ADD_INTERNAL_ATTACHMENT = "internalAttachmentAdd";
    public static final String ACT_ADD_UPDATE_ATTACHMENT   = "updateAttachmentAdd";
    public static final String ACT_EDIT_EXTERNAL_ATTACHMENT = "externalAttachmentEdit";
    public static final String ACT_EDIT_INTERNAL_ATTACHMENT = "internalAttachmentEdit";
    public static final String ACT_EDIT_UPDATE_ATTACHMENT   = "updateAttachmentEdit";

    public static final String ACT_EDIT_ENTITY = "entityEdit";
    public static final String ACT_EDIT_CAT = "categoryEdit";
    public static final String ACT_EDIT_ITEM = "itemEdit";
    public static final String ACT_EDIT_TOPIC = "topicEdit";
    public static final String ACT_EDIT_TYPE = "typeEdit";
    public static final String ACT_EDIT_FILTER = "filterEdit";

    public static final String ACT_VIEW_HOME = "newsHome";
    public static final String ACT_VIEW_AUDIENCE = "viewAudience";
    public static final String ACT_VIEW_C_SETTING = "viewCategorySetting";
    public static final String ACT_VIEW_CAT = "viewCategory";
    public static final String ACT_VIEW_ENTITY = "viewEntity";
    public static final String ACT_VIEW_E_SETTING = "viewEntitySetting";
    public static final String ACT_VIEW_ITEM = "viewItem";
    public static final String ACT_VIEW_M = "viewManagers";
    public static final String ACT_VIEW_NEWSSTORE = "newsStore";
    public static final String ACT_VIEW_NOT_AUTH = "notAuthorized";
    public static final String ACT_VIEW_PERM = "viewPermission";
    public static final String ACT_VIEW_ROLES = "viewRoles";
    public static final String ACT_VIEW_T_SETTING = "viewTopicSetting";
    public static final String ACT_VIEW_TOPIC = "viewTopic";
    public static final String ACT_VIEW_TYPE = "viewTypes";

    public static final String ACT_VIEW_ATT_CONF = "viewAttachmentConf";
    public static final String ACT_VIEW_E_ATT_CONF = "viewEntityAttachmentConf";
    public static final String ACT_EDIT_ATT_OPTIONS = "editAttachmentOptions";
    public static final String ACT_EDIT_E_ATT_OPTIONS = "editEntityAttachmentOptions";
    public static final String ACT_EDIT_CMIS_SERV_PARAMS = "editCmisServerParams";
    public static final String ACT_EDIT_E_CMIS_SERV_PARAMS = "editEntityCmisServerParams";

    public static final String ACT_VIEW_TYPE_SETTING = "viewTypeSetting";
    public static final String ACT_VIEW_FILTERS = "viewFilters";
    public static final String ACT_VIEW_USER_D = "userDetails";
    public static final String ACT_VIEW_XMLTOPICS = "xmlTopicsFeeds";

    public static final String ATT_C_LIST = "categoryList";
    public static final String ATT_E_LIST = "entityList";
    public static final String ATT_I_LIST = "itemList";
    public static final String ATT_A_LIST = "attachmentList";
    public static final String ATT_LDAP_DISPLAY = "attrDisplay";
    public static final String ATT_LIST = "lists";
    public static final String ATT_ROLE_LIST = "roleList";
    public static final String ATT_SUSER_LIST = "sUsers";
    public static final String ATT_T_LIST = "topicList";
    public static final String ATT_USER_LIST = "userList";
    public static final String ATT_TYPE_LIST = "typeList";
    public static final String ATT_FILTER_MAP = "filterMap";
    public static final String ATT_FILTER_TYPE = "filterTypeList";
    public static final String ATT_FILTER_OPERATOR = "operatorList";
    public static final String ATT_FILTER_LDAP_ATTRS = "attrLdapFilter";
    public static final String ATT_MAX_SIZE = "maxSize";
    public static final String ATT_AUTH_EXTS = "authorizedExts";
    public static final String ATT_FORB_EXTS = "forbiddenExts";
    public static final String ATT_SERV_URL = "serverUrl";
    public static final String ATT_SERV_LOGIN = "serverLogin";
    public static final String ATT_SERV_PWD = "serverPwd";
    public static final String ATT_SERV_REPO_ID = "serverRepositoryId";

    public static final String ATT_T_ROLEMAP = "tRoleMap";
    public static final String ATT_C_ROLEMAP = "cRoleMap";
    public static final String ATT_E_ROLEMAP = "eRoleMap";
    public static final String ATT_MAP_C_E = "catListOfEntity";

    public static final String ATT_F = "0";
    public static final String ATT_INC = "increment";
    public static final String ATT_IS_GRP = "isGrp";
    public static final String ATT_NB_DAYS = "nbDays";
    public static final String ATT_NB_ITEM_TO_SHOW = "nbItemsToShow";
    public static final String ATT_PORTAL_URL = "portalURL";
    public static final String ATT_T = "1";
    public static final String ATT_PM = "pMask";
    public static final String ATT_PU = "uMask";
    public static final String ATT_PAGE = "page";
    public static final String ATT_STATUS = "status";
    public static final String ATT_ROLE = "role";
    public static final String ATT_CTX = "ctx";

    public static final String ATT_USER_ID = "userId";
    public static final String ATT_ITEM_ID = "iId";
    public static final String ATT_CAT_ID = "cId";
    public static final String ATT_TOPIC_ID = "tId";
    public static final String ATT_TYPE_ID = "typId";
    public static final String ATT_CTX_ID = "ctxId";
    public static final String ATT_ENTITY_ID = "eId";
    public static final String ATT_FILTER_ID = "typId";

    public static final String OBJ_USER = "user";
    public static final String OBJ_ITEM = "item";
    public static final String OBJ_TOPIC = "topic";
    public static final String OBJ_CATEGORY = "category";
    public static final String OBJ_ENTITY = "entity";
    public static final String OBJ_FILTER = "filter";
    public static final String CMD_ENTITY = "entityForm";
    public static final String CMD_CATEGORY = "categoryForm";
    public static final String CMD_ITEM = "itemForm";
    public static final String CMD_ATTACHMENT = "attachmentForm";
    public static final String CMD_ATTACHMENT_OPTS = "attachmentOptionsForm";
    public static final String CMD_CMIS_SERV_PARAMS = "cmisServerParamsForm";
    public static final String CMD_PERM = "permForm";
    public static final String CMD_SUB_F = "subForm";
    public static final String CMD_SUBSCRIBER = "subscriber";
    public static final String CMD_TYPE = "typeForm";

    public static final String CONST_N = "N";
    public static final String CONST_Y = "Y";
    public static final String DATE_FORMAT = "date.format";
    public static final String ERRORS = "errors";
    public static final String MSG_ERROR = "msgError";

}
