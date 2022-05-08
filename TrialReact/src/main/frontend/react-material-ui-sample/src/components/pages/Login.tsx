import { Paper, TableContainer } from "@mui/material";
import React from "react";
import { DashboardLayout } from "../templates/dashboard/DashboardLayout";
import SignIn from "../templates/sign-in/SignIn";
const LoginPage: React.FC = () => {
  return (
    <DashboardLayout title="ログイン">
      <Paper sx={{ p: 2 }}>
        <SignIn></SignIn>
      </Paper>
    </DashboardLayout>

  );
};

export default LoginPage;
