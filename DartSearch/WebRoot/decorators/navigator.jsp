<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<TABLE class="ms-main" height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0"> <!-- Banner -->
	<TR vAlign="top" height="100%"> <!-- Navigation -->
		<TD class="ms-nav" height="100%">
			<TABLE class="ms-navframe" style="PADDING-TOP: 8px" height="100%" cellSpacing="0" cellPadding="0" width="100%" border="0">
					<TR vAlign="top">
						<TD width="4"><IMG height="1" alt="" src="/websurvey/Images/blank.gif" width="1"></TD>
						<TD class="ms-viewselect">
							<TABLE cellSpacing="0" cellPadding="0" width="100%" border="0">
								<TR>
									<TD id="L_SelectView" style="PADDING-BOTTOM: 2px; PADDING-TOP: 2px">navigator</TD>
								</TR>
								<TR>
									<TD class="ms-navline"><IMG height="1" alt="" src="/websurvey/Images/blank.gif" width="1"></TD>
								</TR>
							</TABLE>
							<decorator:body />
						</TD>
					</TR>
			 </TABLE>							
		</TD>
	</TR>
</TABLE>