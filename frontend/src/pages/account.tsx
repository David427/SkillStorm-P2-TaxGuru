import {
  Form,
  Grid,
  Label,
  Button,
  Select,
  Fieldset,
  TextInput,
  GridContainer,
} from "@trussworks/react-uswds";
import { FormEvent } from "react";

import { states } from "../states";

export default function Account() {
  const username = "Clemente";

  const handleAccountSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    return;
  };

  const handlePasswordChange = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    return;
  };

  return (
    <main className="full-page">
      <div className="bg-base-lightest">
        <GridContainer className="usa-section">
          <h1>Welcome, {username}</h1>

          <Form onSubmit={handleAccountSubmit} className="w-full">
            <Fieldset
              legend="View and manage your account details"
              legendStyle="default"
            >
              {/* First, Last Name, & Email */}
              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="first_name">First Name</Label>
                  <TextInput
                    id="first_name"
                    name="first_name"
                    type="text"
                    defaultValue={"Clemente"}
                    autoComplete="given-name"
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="last_name">Last Name</Label>
                  <TextInput
                    id="last_name"
                    name="last_name"
                    type="text"
                    defaultValue={"Solorio"}
                    autoComplete="family-name"
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="email">Email Address</Label>
                  <TextInput
                    id="email"
                    name="email"
                    type="email"
                    defaultValue={"clem@taxguru.com"}
                    autoComplete="email"
                  />
                </Grid>
              </Grid>

              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="street_address">Street Address</Label>
                  <TextInput
                    id="street_address"
                    name="street_address"
                    type="text"
                    autoComplete="street-address"
                    defaultValue={"123 Park Ln"}
                  />
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="city">City</Label>
                  <TextInput
                    id="city"
                    name="city"
                    type="text"
                    defaultValue={"Anaheim"}
                  />
                </Grid>
              </Grid>

              <Grid row gap>
                <Grid tablet={{ col: true }}>
                  <Label htmlFor="state">State</Label>
                  <Select id="state" name="state">
                    <option>- Select -</option>
                    {states.map((s) => (
                      <option key={s} value={s}>
                        {s}
                      </option>
                    ))}
                  </Select>
                </Grid>

                <Grid tablet={{ col: true }}>
                  <Label htmlFor="zipcode">Zip Code</Label>
                  <TextInput
                    id="zipcode"
                    name="zipcode"
                    type="text"
                    autoComplete="postal-code"
                  />
                </Grid>
              </Grid>

              <Button type="submit">Save Changes</Button>
            </Fieldset>
          </Form>
        </GridContainer>
      </div>
      {/* New Password */}
      <GridContainer>
        <h2>Change Password</h2>

        <Form onSubmit={handlePasswordChange}>
          <Fieldset
            legend="Change your existing password"
            legendStyle="default"
          >
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="current-password">Current Password</Label>
                <TextInput
                  id="current-password"
                  name="current-password"
                  type="password"
                />
              </Grid>
            </Grid>
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="new-password">New Password</Label>
                <TextInput
                  id="new-password"
                  name="new-password"
                  type="password"
                />
              </Grid>
            </Grid>
            <Grid row>
              <Grid tablet={{ col: true }}>
                <Label htmlFor="new-password-confirm">
                  Confirm New Password
                </Label>
                <TextInput
                  id="new-password-confirm"
                  name="new-password-confirm"
                  type="password"
                />
              </Grid>
            </Grid>

            <Button type="submit">Change Password</Button>
          </Fieldset>
        </Form>
      </GridContainer>
    </main>
  );
}
